package com.example.githubuserbrowser.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys { val DARK_MODE = booleanPreferencesKey("dark_mode") }

    val isDarkMode: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.DARK_MODE] ?: false
    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[Keys.DARK_MODE] = enabled }
    }

    suspend fun toggle() {
        dataStore.edit { prefs ->
            val current = prefs[Keys.DARK_MODE] ?: false
            prefs[Keys.DARK_MODE] = !current
        }
    }
}
