package com.example.githubuserbrowser.data.remote

import com.example.githubuserbrowser.data.remote.dto.GithubUserDetailDto
import com.example.githubuserbrowser.data.remote.dto.GithubUserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.githubuserbrowser.data.remote.dto.SearchUsersResponse

interface GithubApiService {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 30
    ): SearchUsersResponse

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Long = 0,
        @Query("per_page") perPage: Int = 30
    ): List<GithubUserDto>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): GithubUserDetailDto
}
