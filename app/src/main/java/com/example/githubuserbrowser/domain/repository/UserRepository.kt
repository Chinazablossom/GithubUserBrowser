package com.example.githubuserbrowser.domain.repository

import com.example.githubuserbrowser.domain.model.User
import com.example.githubuserbrowser.domain.model.UserDetail
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUsers(
        since: Long = 0,
        perPage: Int = 30,
        forceRefresh: Boolean = false
    ): Flow<Result<Pair<List<User>, Boolean>>>

    fun getUserDetail(
        username: String,
        forceRefresh: Boolean = false
    ): Flow<Result<UserDetail>>

    suspend fun searchUsers(query: String, perPage: Int = 30): Result<List<User>>

    suspend fun clearCache()
}
