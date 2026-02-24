package com.example.githubuserbrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Long,
    val login: String,
    val avatarUrl: String?,
    val type: String,
    val cachedAt: Long = System.currentTimeMillis()
)
