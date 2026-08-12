package com.piea.student.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = Constants.PREFS_NAME)

class PreferencesManager(private val context: Context) {

    private val darkModeKey = booleanPreferencesKey(Constants.KEY_DARK_MODE)
    private val rememberMeKey = booleanPreferencesKey("remember_me_enabled")
    private val savedEmailKey = stringPreferencesKey("remember_me_email")
    private val savedPasswordKey = stringPreferencesKey("remember_me_password")
    private val biometricEnabledKey = booleanPreferencesKey("biometric_login_enabled")

    val isDarkModeEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[darkModeKey] ?: false
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[darkModeKey] = enabled }
    }

    /** Saves credentials locally so the Login screen can pre-fill them next time. */
    suspend fun saveRememberedCredentials(email: String, password: String) {
        context.dataStore.edit { prefs ->
            prefs[rememberMeKey] = true
            prefs[savedEmailKey] = email
            prefs[savedPasswordKey] = password
        }
    }

    suspend fun clearRememberedCredentials() {
        context.dataStore.edit { prefs ->
            prefs[rememberMeKey] = false
            prefs.remove(savedEmailKey)
            prefs.remove(savedPasswordKey)
            prefs[biometricEnabledKey] = false
        }
    }

    suspend fun getRememberedEmail(): String = context.dataStore.data.first()[savedEmailKey] ?: ""
    suspend fun getRememberedPassword(): String = context.dataStore.data.first()[savedPasswordKey] ?: ""
    suspend fun isRememberMeEnabled(): Boolean = context.dataStore.data.first()[rememberMeKey] ?: false

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[biometricEnabledKey] = enabled }
    }

    suspend fun isBiometricEnabled(): Boolean = context.dataStore.data.first()[biometricEnabledKey] ?: false
}
