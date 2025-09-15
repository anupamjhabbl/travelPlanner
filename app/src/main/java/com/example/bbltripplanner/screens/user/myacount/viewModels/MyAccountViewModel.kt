package com.example.bbltripplanner.screens.user.myacount.viewModels

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

class MyAccountViewModel(
    private val authPreferencesUseCase: AuthPreferencesUseCase,
    private val profileUseCase: ProfileUseCase
) : BaseMVIVViewModel<MyAccountIntent.ViewEvent>() {
    private val _viewState: MutableSharedFlow<MyAccountIntent.ViewState> = MutableSharedFlow()
    val viewState: SharedFlow<MyAccountIntent.ViewState> = _viewState.asSharedFlow()

    fun getUser(): User? {
        return authPreferencesUseCase.getLoggedUser()
    }

    private fun logOutUser() {
        viewModelScope.launch {
            _viewState.emit(MyAccountIntent.ViewState.Loading)
            val logOutResult = SafeIOUtil.safeCall {
                profileUseCase.logoutUser()
            }
            logOutResult.onSuccess {
                authPreferencesUseCase.clearUserData()
                _viewState.emit(MyAccountIntent.ViewState.LogoutSuccess)
            }
            logOutResult.onFailure {
                _viewState.emit(MyAccountIntent.ViewState.LogoutFailure)
            }
        }
    }

    override fun processEvent(viewEvent: MyAccountIntent.ViewEvent) {
        when (viewEvent) {
            MyAccountIntent.ViewEvent.LogoutUser -> logOutUser()
        }
    }
}