package com.example.bbltripplanner.screens.user.auth.usecases

import com.example.bbltripplanner.common.infra.EncryptedPreferenceManager

class AuthPreferencesUseCase(
    private val encryptedPreferenceManager: EncryptedPreferenceManager
) {
    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    fun saveAccessToken(accessToken: String) {
        encryptedPreferenceManager.saveToken(ACCESS_TOKEN, accessToken)
    }

    fun getAccessToken(): String {
        return encryptedPreferenceManager.getToken(ACCESS_TOKEN) ?: ""
    }

    fun saveRefreshToken(refreshToken: String) {
        encryptedPreferenceManager.saveToken(REFRESH_TOKEN, refreshToken)
    }

    fun getRefreshToken(): String? {
        return encryptedPreferenceManager.getToken(REFRESH_TOKEN)
    }

    fun removeAccessToken() {
        encryptedPreferenceManager.remove(ACCESS_TOKEN)
    }

    fun removeRefreshToken() {
        encryptedPreferenceManager.remove(REFRESH_TOKEN)
    }
}