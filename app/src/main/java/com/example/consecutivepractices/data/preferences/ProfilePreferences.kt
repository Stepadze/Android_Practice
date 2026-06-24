package com.example.consecutivepractices.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfilePreferences(private val context: Context) {

    companion object {
        val KEY_NAME = stringPreferencesKey("profile_name")
        val KEY_POSITION = stringPreferencesKey("profile_position")
        val KEY_RESUME_URL = stringPreferencesKey("profile_resume_url")
        val KEY_AVATAR_URI = stringPreferencesKey("profile_avatar_uri")
        val KEY_PAIR_TIME = stringPreferencesKey("profile_pair_time")
    }

    val userProfile: Flow<UserProfile> = context.dataStore.data.map { prefs ->
        UserProfile(
            name = prefs[KEY_NAME] ?: "",
            position = prefs[KEY_POSITION] ?: "",
            resumeUrl = prefs[KEY_RESUME_URL] ?: "",
            avatarUri = prefs[KEY_AVATAR_URI] ?: "",
            pairTime = prefs[KEY_PAIR_TIME] ?: ""
        )
    }

    suspend fun saveProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NAME] = profile.name
            prefs[KEY_POSITION] = profile.position
            prefs[KEY_RESUME_URL] = profile.resumeUrl
            prefs[KEY_AVATAR_URI] = profile.avatarUri
            prefs[KEY_PAIR_TIME] = profile.pairTime
        }
    }
}