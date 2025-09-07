package com.example.bbltripplanner.screens.user.auth.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.utils.StringUtils.isStrongPassword
import com.example.bbltripplanner.screens.user.auth.entity.UserPasswordResetBody
import com.example.bbltripplanner.screens.user.auth.entity.UserResetPasswordFormState
import com.example.bbltripplanner.screens.user.auth.usecases.UserAuthUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PasswordResetVieModel(
    private val userAuthUseCase: UserAuthUseCase
): BaseMVIVViewModel<UserAuthIntent.ResetPasswordAuth.ViewEvent>() {
    private val _state: MutableStateFlow<UserResetPasswordFormState> = MutableStateFlow(UserResetPasswordFormState())
    val state: StateFlow<UserResetPasswordFormState> = _state.asStateFlow()

    private val _userResetPasswordRequestStatus: MutableSharedFlow<RequestStatus<String>> = MutableSharedFlow()
    val userResetPasswordRequestStatus: SharedFlow<RequestStatus<String>> = _userResetPasswordRequestStatus.asSharedFlow()

    override fun processEvent(viewEvent: UserAuthIntent.ResetPasswordAuth.ViewEvent) {
        when (viewEvent) {
            UserAuthIntent.ResetPasswordAuth.ViewEvent.ResetPassword -> resetPassword()
            is UserAuthIntent.ResetPasswordAuth.ViewEvent.UpdatePassword -> updatePassword(viewEvent.password)
            is UserAuthIntent.ResetPasswordAuth.ViewEvent.UpdateConfirmPassword -> updateConfirmPassword(viewEvent.password)
        }
    }

    private fun resetPassword() {
        viewModelScope.launch {
            _userResetPasswordRequestStatus.emit(RequestStatus.Loading)
            val registerUserResult = withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    userAuthUseCase.resetPassword(getResetPasswordBody())
                }
            }
            registerUserResult.onSuccess {
                _userResetPasswordRequestStatus.emit(RequestStatus.Success(""))
            }
            registerUserResult.onFailure { exception ->
                when (exception) {
                    is TripPlannerException -> {
                        _userResetPasswordRequestStatus.emit(RequestStatus.Error(exception.message))
                    }
                    is Exception -> {
                        _userResetPasswordRequestStatus.emit(RequestStatus.Error(Constants.DEFAULT_ERROR))
                    }
                }
            }
        }
    }

    private fun getResetPasswordBody(): UserPasswordResetBody {
        return UserPasswordResetBody(
            password = state.value.password
        )
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        _state.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordValid = confirmPassword == it.password
            )
        }
    }

    private fun updatePassword(password: String) {
        _state.update {
            it.copy(
                password = password,
                passwordValid = password.isStrongPassword(),
                confirmPasswordValid = password == it.confirmPassword
            )
        }
    }
}