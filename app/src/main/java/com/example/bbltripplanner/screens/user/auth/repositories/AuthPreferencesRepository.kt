package com.example.bbltripplanner.screens.user.auth.repositories

import com.example.bbltripplanner.common.entity.User

interface AuthPreferencesRepository {
    fun isUserLogged(): Boolean
    fun getUserIdLogged(): String?
    fun getLoggedUser(): User?
    fun setUser(loggedUser: User)
    fun removeUserData()
    fun removeRefreshToken()
    fun removeAccessToken()
    fun getRefreshToken(): String?
    fun saveRefreshToken(refreshToken: String)
    fun getAccessToken(): String
    fun saveAccessToken(accessToken: String)
    fun clearUserData()
}