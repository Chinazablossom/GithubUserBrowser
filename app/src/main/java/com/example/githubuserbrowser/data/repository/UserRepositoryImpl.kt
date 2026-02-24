package com.example.githubuserbrowser.data.repository

import com.example.githubuserbrowser.data.local.GithubDatabase
import com.example.githubuserbrowser.data.local.entity.UserEntity
import com.example.githubuserbrowser.data.mapper.toDomain
import com.example.githubuserbrowser.data.mapper.toEntity
import com.example.githubuserbrowser.data.remote.GithubApiService
import com.example.githubuserbrowser.domain.model.User
import com.example.githubuserbrowser.domain.model.UserDetail
import com.example.githubuserbrowser.domain.repository.UserRepository

import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: GithubApiService,
    private val database: GithubDatabase
) : UserRepository {

    companion object {
        private const val HTTP_RATE_LIMIT = 403
        private const val HTTP_NOT_FOUND = 404
    }

    override suspend fun getUsers(
        since: Long,
        perPage: Int,
        forceRefresh: Boolean
    ): Result<List<User>> {
        return try {
            val users = apiService.getUsers(since, perPage)

            if (users.isNotEmpty()) {
                val entities = users.map { it.toEntity() }
                database.userDao().insertUsers(entities)
            }

            Result.success(users.map { it.toDomain() })
        } catch (e: HttpException) {
            handleHttpException(e, since, perPage)
        } catch (e: IOException) {
            fallbackToCache(since, perPage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserDetail(
        username: String,
        forceRefresh: Boolean
    ): Result<UserDetail> {
        return try {
            val userDetail = apiService.getUserDetail(username)
            val entity = userDetail.toEntity()
            database.userDetailDao().insertUserDetail(entity)
            Result.success(userDetail.toDomain())
        } catch (e: HttpException) {
            when (e.code()) {
                HTTP_NOT_FOUND -> Result.failure(
                    Exception("User not found")
                )
                HTTP_RATE_LIMIT -> {
                    val cached = database.userDetailDao().getUserDetailOnce(username)
                    if (cached != null) {
                        Result.success(cached.toDomain())
                    } else {
                        Result.failure(
                            Exception("API rate limit exceeded. Please try again later.")
                        )
                    }
                }
                else -> Result.failure(e)
            }
        } catch (e: IOException) {

            val cached = database.userDetailDao().getUserDetailOnce(username)
            if (cached != null) {
                Result.success(cached.toDomain())
            } else {
                Result.failure(Exception("No internet connection. Please check your network and try again."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUsers(query: String, perPage: Int): Result<List<User>> {
        return try {
            val response = apiService.searchUsers(query = query, perPage = perPage)
            Result.success(response.items.map { it.toDomain() })
        } catch (e: HttpException) {
            // Fallback to local filter on rate limit
            if (e.code() == HTTP_RATE_LIMIT) {
                val cached = database.userDao().getAllUsersOnce().filter {
                    it.login.contains(query, ignoreCase = true)
                }
                if (cached.isNotEmpty()) Result.success(cached.map { it.toDomain() })
                else Result.failure(Exception("API rate limit exceeded. Please try again later."))
            } else Result.failure(e)
        } catch (e: IOException) {
            val cached = database.userDao().getAllUsersOnce().filter {
                it.login.contains(query, ignoreCase = true)
            }
            if (cached.isNotEmpty()) Result.success(cached.map { it.toDomain() })
            else Result.failure(Exception("No internet connection. Please check your network and try again."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearCache() {
        database.userDao().clearAll()
        database.userDetailDao().clearAll()
    }

    private suspend fun handleHttpException(
        e: HttpException,
        since: Long,
        perPage: Int
    ): Result<List<User>> {
        return when (e.code()) {
            HTTP_RATE_LIMIT -> {
                val cached = database.userDao().getUsersSince(since, perPage)
                if (cached.isNotEmpty()) {
                    Result.success(cached.map { it.toDomain() })
                } else {
                    Result.failure(
                        Exception("API rate limit exceeded. Please try again later.")
                    )
                }
            }
            else -> {
                fallbackToCache(since, perPage)
            }
        }
    }

    private suspend fun fallbackToCache(since: Long, perPage: Int): Result<List<User>> {
        val cached = database.userDao().getUsersSince(since, perPage)
        return if (cached.isNotEmpty()) {
            Result.success(cached.map { it.toDomain() })
        } else {
            Result.failure(
                Exception("No internet connection. Please check your network and try again.")
            )
        }
    }
}
