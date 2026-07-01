package com.example.bbltripplanner.common.infra

import android.content.Context
import android.content.SharedPreferences
import com.example.bbltripplanner.common.entity.User
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PreferenceManager (
    val context: Context,
    val gson: Gson
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val LOCAL_USER_DATA = "local_user_data"
        private const val PREFS_NAME = "basic_prefs"
        const val APP_THEME = "app_theme"
    }

    object ThemeType {
        const val SYSTEM = "system"
        const val LIGHT = "light"
        const val DARK = "dark"
    }

    fun saveLocalUser(user: User) {
        sharedPreferences.edit().putString(LOCAL_USER_DATA, gson.toJson(user)).apply()
    }

    fun removeLocalUser() {
        sharedPreferences.edit().remove(LOCAL_USER_DATA).apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun getLocalUser(): User? {
        val userData = sharedPreferences.getString(LOCAL_USER_DATA, null)
        return userData?.let {
            gson.fromJson(userData, User::class.java)
        }
    }

    fun updateTripCount(change: Int) {
        getLocalUser()?.let { user ->
            val updatedUser = user.copy(tripCount = (user.tripCount + change).coerceAtLeast(0))
            saveLocalUser(updatedUser)
        }
    }

    fun getAppTheme(): String {
        return sharedPreferences.getString(APP_THEME, ThemeType.SYSTEM) ?: ThemeType.SYSTEM
    }

    fun setAppTheme(theme: String) {
        sharedPreferences.edit().putString(APP_THEME, theme).apply()
    }

    fun getAppThemeFlow(): Flow<String> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == APP_THEME) {
                trySend(getAppTheme())
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(getAppTheme())
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }
}