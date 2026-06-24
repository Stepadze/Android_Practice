package com.example.consecutivepractices.domain.cache

class FilterBadgeCache {
    private var hasActiveFilters: Boolean = false

    fun setHasActiveFilters(value: Boolean) {
        hasActiveFilters = value
    }

    fun getHasActiveFilters(): Boolean = hasActiveFilters
}