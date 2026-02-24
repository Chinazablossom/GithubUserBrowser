package com.example.githubuserbrowser.presentation.state

import com.example.githubuserbrowser.domain.model.UserDetail

data class UserDetailState(
    val userDetail: UserDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)
