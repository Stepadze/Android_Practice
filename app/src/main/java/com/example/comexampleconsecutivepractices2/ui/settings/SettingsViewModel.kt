package com.example.consecutivepractices.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.data.preferences.SearchPreferences
import com.example.consecutivepractices.data.preferences.SearchSettings
import com.example.consecutivepractices.domain.cache.FilterBadgeCache
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val searchPreferences: SearchPreferences,
    private val filterBadgeCache: FilterBadgeCache
) : ViewModel() {

    val searchSettings: StateFlow<SearchSettings> = searchPreferences.searchSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchSettings()
        )

    fun saveSettings(query: String, type: String, year: String) {
        viewModelScope.launch {
            searchPreferences.saveSettings(query, type, year)

            val hasFilters = query != "Batman" || type != "movie" || year.isNotEmpty()
            filterBadgeCache.setHasActiveFilters(hasFilters)
        }
    }
}