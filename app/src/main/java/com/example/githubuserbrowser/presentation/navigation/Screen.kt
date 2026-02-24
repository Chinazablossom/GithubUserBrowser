package com.example.githubuserbrowser.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {

    @Serializable
    data object UserListScreen : Screen()

    @Serializable
    data class UserDetailScreen(val username: String) : Screen()
}
