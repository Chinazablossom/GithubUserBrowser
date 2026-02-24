package com.example.githubuserbrowser.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubUserDto(
    @SerialName("id")
    val id: Long,
    @SerialName("login")
    val login: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("type")
    val type: String = "User",
    @SerialName("url")
    val url: String? = null
)
