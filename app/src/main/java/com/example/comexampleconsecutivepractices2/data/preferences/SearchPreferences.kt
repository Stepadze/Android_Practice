package com.example.consecutivepractices.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_settings")

data class SearchSettings(
    val query: String = "Batman",
    val type: String = "movie",
    val year: String = ""
)

class SearchPreferences(private val context: Context) {

    companion object {
        val KEY_QUERY = stringPreferencesKey("search_query")
        val KEY_TYPE = stringPreferencesKey("search_type")
        val KEY_YEAR = stringPreferencesKey("search_year")
    }

    val searchSettings: Flow<SearchSettings> = context.dataStore.data.map { prefs ->
        SearchSettings(
            query = prefs[KEY_QUERY] ?: "Batman",
            type = prefs[KEY_TYPE] ?: "movie",
            year = prefs[KEY_YEAR] ?: ""
        )
    }

    suspend fun saveSettings(query: String, type: String, year: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_QUERY] = query
            prefs[KEY_TYPE] = type
            prefs[KEY_YEAR] = year
        }
    }
}