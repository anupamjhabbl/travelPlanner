package com.example.bbltripplanner.screens.user.auth.usecases

import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.infra.EncryptedPreferenceManager
import com.example.bbltripplanner.common.infra.PreferenceManager
import com.example.bbltripplanner.screens.user.auth.repositories.AuthPreferencesRepository

class AuthPreferencesUseCase(
    private val encryptedPreferenceManager: EncryptedPreferenceManager,
    private val preferenceManager: PreferenceManager
): AuthPreferencesRepository {
    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    override fun saveAccessToken(accessToken: String) {
        encryptedPreferenceManager.saveToken(ACCESS_TOKEN, accessToken)
    }

    override fun getAccessToken(): String {
        return encryptedPreferenceManager.getToken(ACCESS_TOKEN) ?: ""
    }

    override fun saveRefreshToken(refreshToken: String) {
        encryptedPreferenceManager.saveToken(REFRESH_TOKEN, refreshToken)
    }

    override fun getRefreshToken(): String? {
        return encryptedPreferenceManager.getToken(REFRESH_TOKEN)
    }

    override fun removeAccessToken() {
        encryptedPreferenceManager.remove(ACCESS_TOKEN)
    }

    override fun removeRefreshToken() {
        encryptedPreferenceManager.remove(REFRESH_TOKEN)
    }

    override fun isUserLogged(): Boolean {
        return getLoggedUser() != null
    }

    override fun getUserIdLogged(): String? {
        return getLoggedUser()?.id
    }

    override fun getLoggedUser(): User? {
        return preferenceManager.getLocalUser()
    }

    override fun saveLoggedUser(loggedUser: User) {
        preferenceManager.saveLocalUser(loggedUser)
    }

    override fun removeUserData() {
        preferenceManager.removeLocalUser()
    }

    override fun clearUserData() {
        preferenceManager.clear()
        encryptedPreferenceManager.clear()
    }
}