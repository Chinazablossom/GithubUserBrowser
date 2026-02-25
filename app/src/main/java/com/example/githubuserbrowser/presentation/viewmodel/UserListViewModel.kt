package com.example.githubuserbrowser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserbrowser.domain.model.User
import com.example.githubuserbrowser.domain.usecase.GetUsersUseCase
import com.example.githubuserbrowser.domain.usecase.SearchUsersUseCase
import com.example.githubuserbrowser.presentation.state.UserListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isSearching = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(UserListState())
    val uiState: StateFlow<UserListState> = _uiState.asStateFlow()
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var isInitialLoadDone = false

    init {
        // Search debounce
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .collect { q ->
                    if (_isSearching.value) executeSearch(q)
                }
        }

        // Load initial data
        loadUsers()
    }


    fun loadUsers(refresh: Boolean = false) {
        if (refresh) {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            requestLoad(isRefresh = true)
        } else if (!isInitialLoadDone) {
            _uiState.update { it.copy(isLoading = true, error = null) }
            requestLoad(isRefresh = false)
        }
    }

    private fun requestLoad(isRefresh: Boolean) {
        viewModelScope.launch {
            val since = if (isRefresh) 0L else _uiState.value.currentSince
            val forceRefresh = isRefresh

            getUsersUseCase(
                since = since,
                perPage = 30,
                forceRefresh = forceRefresh
            ).collectLatest { result ->
                result
                    .onSuccess { (users, paginationInfo) ->
                        _uiState.update { currentState ->
                            val newUsers = if (since == 0L) {
                                users // Replace for initial load or refresh
                            } else {
                                (currentState.users + users).distinctBy { it.id }
                            }

                            currentState.copy(
                                users = newUsers,
                                isLoading = false,
                                isLoadingMore = false,
                                isRefreshing = false,
                                isEmpty = newUsers.isEmpty(),
                                currentSince = paginationInfo.currentSince,
                                hasMore = paginationInfo.hasMore,
                                error = null
                            )
                        }
                        isInitialLoadDone = true
                    }
                    .onFailure { exception ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                isRefreshing = false,
                                error = exception.message ?: "An error occurred",
                                isEmpty = currentState.users.isEmpty()
                            )
                        }
                    }
            }
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState.isLoadingMore || !currentState.hasMore || currentState.isRefreshing) {
            return
        }

        _uiState.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            getUsersUseCase(
                since = currentState.currentSince,
                perPage = 30,
                forceRefresh = false
            ).collectLatest { result ->
                result
                    .onSuccess { (users, paginationInfo) ->
                        _uiState.update { state ->
                            val newUsers = (state.users + users).distinctBy { it.id }
                            state.copy(
                                users = newUsers,
                                isLoading = false,
                                isLoadingMore = false,
                                isRefreshing = false,
                                isEmpty = newUsers.isEmpty(),
                                currentSince = paginationInfo.currentSince,
                                hasMore = paginationInfo.hasMore,
                                error = null
                            )
                        }
                    }
                    .onFailure { exception ->
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                isRefreshing = false,
                                error = exception.message ?: "An error occurred"
                            )
                        }
                    }
            }
        }
    }

    fun retry() {
        _uiState.update { it.copy(error = null, isLoading = true) }
        val since = if (_uiState.value.users.isEmpty()) 0L else _uiState.value.currentSince
        viewModelScope.launch {
            getUsersUseCase(
                since = since,
                perPage = 30,
                forceRefresh = false
            ).collectLatest { result ->
                result
                    .onSuccess { (users, paginationInfo) ->
                        _uiState.update { currentState ->
                            val newUsers = if (since == 0L) users else (currentState.users + users).distinctBy { it.id }
                            currentState.copy(
                                users = newUsers,
                                isLoading = false,
                                isLoadingMore = false,
                                isRefreshing = false,
                                isEmpty = newUsers.isEmpty(),
                                currentSince = paginationInfo.currentSince,
                                hasMore = paginationInfo.hasMore,
                                error = null
                            )
                        }
                        isInitialLoadDone = true
                    }
                    .onFailure { exception ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                isRefreshing = false,
                                error = exception.message ?: "An error occurred",
                                isEmpty = currentState.users.isEmpty()
                            )
                        }
                    }
            }
        }
    }


    fun enableSearch(enable: Boolean) {
        _isSearching.value = enable
        if (!enable) {
            _searchQuery.value = ""
            // Return to list mode
            if (uiState.value.users.isEmpty()) loadUsers()
        }
    }

    fun updateSearchQuery(q: String) { _searchQuery.value = q }

    private suspend fun executeSearch(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(error = null, isEmpty = it.users.isEmpty()) }
            return
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        searchUsersUseCase(query)
            .onSuccess { users ->
                _uiState.update { it.copy(users = users, isLoading = false, isEmpty = users.isEmpty(), isLoadingMore = false, isRefreshing = false) }
            }
            .onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Search failed") }
            }
    }
}