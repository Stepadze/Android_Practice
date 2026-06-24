package com.example.consecutivepractices.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_prefs")

class SearchPreferences(private val context: Context) {

    companion object {
        val KEY_QUERY = stringPreferencesKey("search_query")
        val KEY_TYPE = stringPreferencesKey("search_type")
        val KEY_YEAR = stringPreferencesKey("search_year")
    }

    val searchQuery: Flow<String> = context.dataStore.data
        .map { it[KEY_QUERY] ?: "Batman" }

    val searchType: Flow<String> = context.dataStore.data
        .map { it[KEY_TYPE] ?: "movie" }

    val searchYear: Flow<String> = context.dataStore.data
        .map { it[KEY_YEAR] ?: "" }

    suspend fun saveSettings(query: String, type: String, year: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_QUERY] = query
            prefs[KEY_TYPE] = type
            prefs[KEY_YEAR] = year
        }
    }
}