package com.example.githubuserbrowser.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserbrowser.domain.usecase.GetUserDetailUseCase
import com.example.githubuserbrowser.presentation.state.UserDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserDetailState())
    val uiState: StateFlow<UserDetailState> = _uiState.asStateFlow()

    private var currentUsername: String? = null

    fun loadUserDetail(username: String, refresh: Boolean = false) {
        if (username == currentUsername && !refresh && _uiState.value.userDetail != null) {
            return // Already loaded
        }

        currentUsername = username

        if (refresh) {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
        } else {
            _uiState.update { it.copy(isLoading = true, error = null) }
        }

        viewModelScope.launch {
            getUserDetailUseCase(username, forceRefresh = refresh)
                .onSuccess { userDetail ->
                    _uiState.update {
                        it.copy(
                            userDetail = userDetail,
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = exception.message ?: "Failed to load user details"
                        )
                    }
                }
        }
    }

    fun retry() {
        currentUsername?.let { username ->
            loadUserDetail(username, refresh = false)
        }
    }


    fun refresh() {
        currentUsername?.let { username ->
            loadUserDetail(username, refresh = true)
        }
    }
}
