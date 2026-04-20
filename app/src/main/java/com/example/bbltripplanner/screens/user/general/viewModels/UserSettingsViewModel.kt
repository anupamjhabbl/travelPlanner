package com.example.bbltripplanner.screens.user.general.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class UserSettingsViewModel(
    private val authPreferencesUseCase: AuthPreferencesUseCase,
    private val profileUseCase: ProfileUseCase
): BaseMVIVViewModel<UserSettingsIntent.ViewEvent>() {
    private val _logOutResultState: MutableSharedFlow<LogOutState> = MutableSharedFlow()
    val logOutResultState: SharedFlow<LogOutState> = _logOutResultState.asSharedFlow()

    override fun processEvent(viewEvent: UserSettingsIntent.ViewEvent) {
        when (viewEvent) {
            UserSettingsIntent.ViewEvent.LogoutUser -> logOutUser()
        }
    }

    fun getUser(): User? {
        return authPreferencesUseCase.getLoggedUser()
    }

    private fun logOutUser() {
        viewModelScope.launch {
            _logOutResultState.emit(LogOutState.LOADING)
            val logOutResult = SafeIOUtil.safeCall {
                profileUseCase.logoutUser()
            }
            logOutResult.onSuccess {
                authPreferencesUseCase.clearUserData()
                _logOutResultState.emit(LogOutState.SUCCESS)
            }
            logOutResult.onFailure {
                _logOutResultState.emit(LogOutState.FAILURE)
            }
        }
    }
}

enum class LogOutState {
    LOADING, SUCCESS, FAILURE
}

class UserSettingsIntent {
    sealed interface ViewEvent {
        data object LogoutUser: ViewEvent
    }
}