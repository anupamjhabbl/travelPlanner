package com.example.bbltripplanner.screens.user.general.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.common.utils.ErrorUtils
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import com.example.bbltripplanner.common.infra.PreferenceManager

class UserSettingsViewModel(
    private val authPreferencesUseCase: AuthPreferencesUseCase,
    private val profileUseCase: ProfileUseCase,
    private val preferenceManager: PreferenceManager
): BaseMVIVViewModel<UserSettingsIntent.ViewEvent>() {
    private val _logOutResultState: MutableSharedFlow<LogOutState> = MutableSharedFlow()
    val logOutResultState: SharedFlow<LogOutState> = _logOutResultState.asSharedFlow()

    val appTheme: StateFlow<String> = preferenceManager.getAppThemeFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, preferenceManager.getAppTheme())

    override fun processEvent(viewEvent: UserSettingsIntent.ViewEvent) {
        when (viewEvent) {
            UserSettingsIntent.ViewEvent.LogoutUser -> logOutUser()
            is UserSettingsIntent.ViewEvent.ChangeTheme -> preferenceManager.setAppTheme(viewEvent.theme)
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
            logOutResult.onFailure { exception ->
                val errorMsg = ErrorUtils.toErrorType(exception)
                _logOutResultState.emit(LogOutState.FAILURE(errorMsg))
            }
        }
    }
}

sealed interface LogOutState {
    data object LOADING: LogOutState
    data object SUCCESS: LogOutState
    data class FAILURE(val errorType: String): LogOutState
}

class UserSettingsIntent {
    sealed interface ViewEvent {
        data object LogoutUser: ViewEvent
        data class ChangeTheme(val theme: String): ViewEvent
    }
}