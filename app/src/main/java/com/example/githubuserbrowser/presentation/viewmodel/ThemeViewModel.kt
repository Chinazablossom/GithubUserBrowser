package com.example.githubuserbrowser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserbrowser.data.local.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val repo: ThemeRepository
): ViewModel() {

    val isDarkMode: StateFlow<Boolean> = repo.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggle() {
        viewModelScope.launch { repo.toggle() }
    }

    fun set(enabled: Boolean) {
        viewModelScope.launch { repo.setDarkMode(enabled) }
    }
}
