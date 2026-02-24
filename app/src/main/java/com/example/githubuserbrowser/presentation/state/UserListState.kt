package com.example.githubuserbrowser.presentation.state

import com.example.githubuserbrowser.domain.model.User

data class UserListState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false,
    val currentSince: Long = 0,
    val hasMore: Boolean = true,
    val isRefreshing: Boolean = false
)
