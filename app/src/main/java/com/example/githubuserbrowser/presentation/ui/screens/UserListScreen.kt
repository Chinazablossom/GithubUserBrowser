package com.example.githubuserbrowser.presentation.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.githubuserbrowser.R
import com.example.githubuserbrowser.domain.model.User
import com.example.githubuserbrowser.presentation.state.UserListState
import com.example.githubuserbrowser.presentation.ui.components.EmptyScreen
import com.example.githubuserbrowser.presentation.ui.components.ErrorScreen
import com.example.githubuserbrowser.presentation.ui.components.LoadingIndicator
import com.example.githubuserbrowser.presentation.ui.components.PaginationLoadingIndicator
import com.example.githubuserbrowser.presentation.ui.components.UserListItem
import com.example.githubuserbrowser.presentation.viewmodel.UserListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    onUserClick: (User) -> Unit,
    onToggleTheme: () -> Unit,
    isDarkMode: Boolean, modifier: Modifier = Modifier,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()


    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            // Loads more when we're within 5 items of the end
            lastVisibleItemIndex > (totalItemsNumber - 5) &&
                    totalItemsNumber > 0 &&
                    uiState.hasMore &&
                    !uiState.isLoadingMore &&
                    !uiState.isRefreshing
        }
    }

    // Trigger load more when needed
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMore()
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
                    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
                    if (isSearching) {
                        TextField(
                            value = query,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = { Text(stringResource(R.string.search_hint)) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
                    if (!isSearching) {
                        IconButton(onClick = { viewModel.enableSearch(true) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search)
                            )
                        }
                    } else {
                        IconButton(onClick = { viewModel.enableSearch(false) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = android.R.string.cancel)
                            )
                        }
                    }
                    IconButton(onClick = { viewModel.loadUsers(refresh = true) }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh_button)
                        )
                    }
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = stringResource(
                                if (isDarkMode) R.string.light_mode else R.string.dark_mode
                            )
                        )
                    }
                    IconButton(onClick = { viewModel.clearCache() }) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = stringResource(R.string.clear_cache)
                        )
                    }

                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = stringResource(
                            if (isDarkMode) R.string.light_mode else R.string.dark_mode
                        )
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        UserListContent(
            uiState = uiState,
            lazyListState = lazyListState,
            onUserClick = onUserClick,
            onRetry = { viewModel.retry() },
            onRefresh = { viewModel.loadUsers(refresh = true) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserListContent(
    uiState: UserListState,
    lazyListState: androidx.compose.foundation.lazy.LazyListState,
    onUserClick: (User) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Box(modifier = modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    isRefreshing = uiState.isRefreshing,
                    state = pullToRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            AnimatedContent(
                targetState = when {
                    uiState.isLoading && uiState.users.isEmpty() -> ScreenState.LOADING
                    uiState.error != null && uiState.users.isEmpty() -> ScreenState.ERROR
                    uiState.isEmpty -> ScreenState.EMPTY
                    else -> ScreenState.CONTENT
                },
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                },
                modifier = Modifier.fillMaxSize()
            ) { state ->
                when (state) {
                    ScreenState.LOADING -> {
                        LoadingIndicator(isLoading = true)
                    }

                    ScreenState.ERROR -> {
                        ErrorScreen(
                            message = uiState.error ?: stringResource(R.string.unknown_error),
                            onRetry = onRetry
                        )
                    }

                    ScreenState.EMPTY -> {
                        EmptyScreen()
                    }

                    ScreenState.CONTENT -> {
                        UserList(
                            users = uiState.users,
                            isLoadingMore = uiState.isLoadingMore,
                            lazyListState = lazyListState,
                            onUserClick = onUserClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserList(
    users: List<User>,
    isLoadingMore: Boolean,
    lazyListState: androidx.compose.foundation.lazy.LazyListState,
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(
            items = users,
            key = { it.id }
        ) { user ->
            UserListItem(
                user = user,
                onClick = { onUserClick(user) }
            )
        }

        // Pagination loading indicator at the bottom
        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PaginationLoadingIndicator(isLoading = true)
                }
            }
        }

        // Bottom spacer for better UX
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private enum class ScreenState {
    LOADING,
    ERROR,
    EMPTY,
    CONTENT
}
