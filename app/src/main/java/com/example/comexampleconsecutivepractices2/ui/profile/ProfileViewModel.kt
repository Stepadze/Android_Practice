package com.example.consecutivepractices.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.data.preferences.ProfilePreferences
import com.example.consecutivepractices.data.preferences.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profilePreferences: ProfilePreferences
) : ViewModel() {

    val profile: StateFlow<UserProfile> = profilePreferences.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            profilePreferences.saveProfile(profile)
        }
    }
}