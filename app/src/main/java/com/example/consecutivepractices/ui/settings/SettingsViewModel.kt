package com.example.consecutivepractices.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.data.preferences.SearchPreferences
import com.example.consecutivepractices.domain.cache.FilterBadgeCache
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val searchPreferences: SearchPreferences,
    private val filterBadgeCache: FilterBadgeCache
) : ViewModel() {

    val searchQuery: StateFlow<String> = searchPreferences.searchQuery
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "Batman")

    val searchType: StateFlow<String> = searchPreferences.searchType
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "movie")

    val searchYear: StateFlow<String> = searchPreferences.searchYear
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    fun saveSettings(query: String, type: String, year: String) {
        viewModelScope.launch {
            searchPreferences.saveSettings(query, type, year)
            val hasFilters = query != "Batman" || type != "movie" || year.isNotEmpty()
            filterBadgeCache.setHasActiveFilters(hasFilters)
        }
    }
}