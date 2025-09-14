package com.example.bbltripplanner.main.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.auth.usecases.UserAuthUseCase
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val userAuthUseCase: UserAuthUseCase,
    private val authPreferencesUseCase: AuthPreferencesUseCase
): BaseMVIVViewModel<MainActivityIntent.ViewEvent>() {
    override fun processEvent(viewEvent: MainActivityIntent.ViewEvent) {
        when (viewEvent) {
            MainActivityIntent.ViewEvent.Init -> initialize()
        }
    }

    private fun initialize() {
        getLocalUserData()
    }

    private fun getLocalUserData() {
        if (authPreferencesUseCase.getAccessToken().isNotEmpty()) {
            viewModelScope.launch {
                val result = SafeIOUtil.safeCall {
                    userAuthUseCase.getLocalUser()
                }
                result.onSuccess { loggedUser ->
                    loggedUser?.let {
                        authPreferencesUseCase.saveLoggedUser(it)
                    }
                }
            }
        }
    }
}