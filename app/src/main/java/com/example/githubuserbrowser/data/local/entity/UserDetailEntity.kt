package com.example.githubuserbrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_details")
data class UserDetailEntity(
    @PrimaryKey
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
    val htmlUrl: String?,
    val cachedAt: Long = System.currentTimeMillis()
)
