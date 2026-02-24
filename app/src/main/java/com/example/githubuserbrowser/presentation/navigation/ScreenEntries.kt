package com.example.githubuserbrowser.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.githubuserbrowser.presentation.ui.screens.UserDetailScreen
import com.example.githubuserbrowser.presentation.ui.screens.UserListScreen
import androidx.compose.ui.Modifier

@Composable
fun EntryProviderScope<NavKey>.UserListEntry(
    backStack: NavBackStack<NavKey>,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<Screen.UserListScreen> {
        UserListScreen(
            onUserClick = { user -> backStack += Screen.UserDetailScreen(user.login) },
            onToggleTheme = onToggleTheme,
            isDarkMode = isDarkMode
        )
    }
}

@Composable
fun EntryProviderScope<NavKey>.UserDetailEntry(
    backStack: NavBackStack<NavKey>,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<Screen.UserDetailScreen> { key ->
        UserDetailScreen(
            username = key.username,
            onBackClick = { backStack.removeLastOrNull() },
            onToggleTheme = onToggleTheme,
            isDarkMode = isDarkMode
        )
    }
}
