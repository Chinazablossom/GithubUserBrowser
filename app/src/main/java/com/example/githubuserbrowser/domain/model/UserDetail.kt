package com.example.githubuserbrowser.domain.model

data class UserDetail(
    val id: Long,
    val login: String,
    val avatarUrl: String?,
    val type: String,
    val bio: String?,
    val followers: Int,
    val publicRepos: Int,
    val name: String?,
    val company: String?,
    val location: String?,
    val blog: String?,
    val htmlUrl: String?
)
