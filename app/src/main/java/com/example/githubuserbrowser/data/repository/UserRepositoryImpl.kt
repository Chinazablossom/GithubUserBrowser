package com.example.githubuserbrowser.data.repository

import com.example.githubuserbrowser.data.local.GithubDatabase
import com.example.githubuserbrowser.data.mapper.toDomain
import com.example.githubuserbrowser.data.mapper.toEntity
import com.example.githubuserbrowser.data.remote.GithubApiService
import com.example.githubuserbrowser.domain.model.User
import com.example.githubuserbrowser.domain.model.UserDetail
import com.example.githubuserbrowser.domain.repository.UserRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
    }

    override fun getUsers(
        since: Long,
        perPage: Int,
        forceRefresh: Boolean
    ): Flow<Result<Pair<List<User>, Boolean>>> {
        return database.userDao().getAllUsers().map { cachedUsers ->
            Result.success(Pair(cachedUsers.map { it.toDomain() }, cachedUsers.isNotEmpty()))
        }.onStart {
            try {
                val users = apiService.getUsers(since, perPage)
                if (users.isNotEmpty()) {
                    val entities = users.map { it.toEntity() }
                    database.userDao().insertUsers(entities)
                }
            } catch (_: HttpException) {
            } catch (_: IOException) {
            } catch (_: Exception) {
            }
        }
    }

    override fun getUserDetail(
        username: String,
        forceRefresh: Boolean
    ): Flow<Result<UserDetail>> {
        return database.userDetailDao().getUserDetail(username).map { cachedUser ->
            cachedUser?.let {
                Result.success(it.toDomain())
            } ?: Result.failure(Exception("User not found"))
        }.onStart {
            try {
                val userDetail = apiService.getUserDetail(username)
                val entity = userDetail.toEntity()
                database.userDetailDao().insertUserDetail(entity)
            } catch (_: HttpException) {
            } catch (_: IOException) {
            } catch (_: Exception) {
            }
        }
    }

    override suspend fun searchUsers(query: String, perPage: Int): Result<List<User>> {
        return try {
            val response = apiService.searchUsers(query = query, perPage = perPage)
            Result.success(response.items.map { it.toDomain() })
        } catch (e: HttpException) {

            if (e.code() == HTTP_RATE_LIMIT) {
                val cached = database.userDao().getAllUsersOnce().filter {
                    it.login.contains(query, ignoreCase = true)
                }
                if (cached.isNotEmpty()) Result.success(cached.map { it.toDomain() })
                else Result.failure(Exception("API rate limit exceeded. Please try again later."))
            } else Result.failure(e)
        } catch (_: IOException) {
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
}
