package com.example.githubuserbrowser.domain.model

data class PaginationInfo(
    val currentSince: Long = 0,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false
)
