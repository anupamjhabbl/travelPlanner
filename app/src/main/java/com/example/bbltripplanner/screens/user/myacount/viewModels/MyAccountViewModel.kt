package com.example.bbltripplanner.screens.user.myacount.viewModels

import androidx.lifecycle.ViewModel
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase

class MyAccountViewModel(
    private val authPreferencesUseCase: AuthPreferencesUseCase
) : ViewModel() {
    fun getUser(): User? {
        return authPreferencesUseCase.getLoggedUser()
    }

    fun logOutUser() {
        authPreferencesUseCase.clearUserData()
    }
}