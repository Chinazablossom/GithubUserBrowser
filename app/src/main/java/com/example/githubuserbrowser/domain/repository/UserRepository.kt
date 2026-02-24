package com.example.githubuserbrowser.domain.repository

import com.example.githubuserbrowser.domain.model.User
import com.example.githubuserbrowser.domain.model.UserDetail

interface UserRepository {

    suspend fun getUsers(
        since: Long = 0,
        perPage: Int = 30,
        forceRefresh: Boolean = false
    ): Result<List<User>>

    suspend fun getUserDetail(
        username: String,
        forceRefresh: Boolean = false
    ): Result<UserDetail>

    suspend fun searchUsers(query: String, perPage: Int = 30): Result<List<User>>

    suspend fun clearCache()
}
