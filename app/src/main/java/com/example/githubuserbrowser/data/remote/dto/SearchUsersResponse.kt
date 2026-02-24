package com.example.githubuserbrowser.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchUsersResponse(
    @SerialName("total_count") val totalCount: Int = 0,
    @SerialName("items") val items: List<GithubUserDto> = emptyList()
)
