package com.example.githubuserbrowser.presentation.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.githubuserbrowser.presentation.navigation.Screen
import com.example.githubuserbrowser.presentation.navigation.UserDetailEntry
import com.example.githubuserbrowser.presentation.navigation.UserListEntry

@Composable
fun UserBrowserApp(isDarkMode: Boolean, onToggleTheme: () -> Unit, modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Screen.UserListScreen)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            UserListEntry(backStack = backStack, isDarkMode = isDarkMode, onToggleTheme = onToggleTheme)
            UserDetailEntry(backStack = backStack, isDarkMode = isDarkMode, onToggleTheme = onToggleTheme)
        },
        modifier = Modifier.fillMaxSize()
    )
}
