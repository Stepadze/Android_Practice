package com.example.consecutivepractices.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_settings")

data class UserProfile(
    val name: String = "",
    val position: String = "",
    val resumeUrl: String = "",
    val avatarUri: String = ""
)

class ProfilePreferences(private val context: Context) {

    companion object {
        val KEY_NAME = stringPreferencesKey("profile_name")
        val KEY_POSITION = stringPreferencesKey("profile_position")
        val KEY_RESUME_URL = stringPreferencesKey("profile_resume_url")
        val KEY_AVATAR_URI = stringPreferencesKey("profile_avatar_uri")
    }

    val userProfile: Flow<UserProfile> = context.profileDataStore.data.map { prefs ->
        UserProfile(
            name = prefs[KEY_NAME] ?: "",
            position = prefs[KEY_POSITION] ?: "",
            resumeUrl = prefs[KEY_RESUME_URL] ?: "",
            avatarUri = prefs[KEY_AVATAR_URI] ?: ""
        )
    }

    suspend fun saveProfile(profile: UserProfile) {
        context.profileDataStore.edit { prefs ->
            prefs[KEY_NAME] = profile.name
            prefs[KEY_POSITION] = profile.position
            prefs[KEY_RESUME_URL] = profile.resumeUrl
            prefs[KEY_AVATAR_URI] = profile.avatarUri
        }
    }
}