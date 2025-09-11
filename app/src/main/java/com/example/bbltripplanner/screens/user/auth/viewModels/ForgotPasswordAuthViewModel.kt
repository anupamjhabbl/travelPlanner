package com.example.bbltripplanner.screens.user.auth.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.common.utils.StringUtils.isValidEmail
import com.example.bbltripplanner.screens.user.auth.entity.UserForgetPasswordBody
import com.example.bbltripplanner.screens.user.auth.entity.UserForgetPasswordFormState
import com.example.bbltripplanner.screens.user.auth.entity.UserRegisteredId
import com.example.bbltripplanner.screens.user.auth.usecases.UserAuthUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordAuthViewModel(
    private val userAuthUseCase: UserAuthUseCase
): BaseMVIVViewModel<UserAuthIntent.ForgetPasswordAuth.ViewEvent>() {
    private val _state: MutableStateFlow<UserForgetPasswordFormState> = MutableStateFlow(UserForgetPasswordFormState())
    val state: StateFlow<UserForgetPasswordFormState> = _state.asStateFlow()

    private val _userForgetPasswordRequestStatus: MutableSharedFlow<RequestStatus<UserRegisteredId>> = MutableSharedFlow()
    val userForgetPasswordRequestStatus: SharedFlow<RequestStatus<UserRegisteredId>> = _userForgetPasswordRequestStatus.asSharedFlow()

    override fun processEvent(viewEvent: UserAuthIntent.ForgetPasswordAuth.ViewEvent) {
        when (viewEvent) {
            UserAuthIntent.ForgetPasswordAuth.ViewEvent.ResetPassword -> resetPassword()
            is UserAuthIntent.ForgetPasswordAuth.ViewEvent.UpdateEmail -> updateEmail(viewEvent.email)
        }
    }

    private fun getForgetPasswordRequestBody(): UserForgetPasswordBody {
        return UserForgetPasswordBody(
            email = state.value.email
        )
    }

    private fun  resetPassword() {
        viewModelScope.launch {
            _userForgetPasswordRequestStatus.emit(RequestStatus.Loading)
            val registerUserResult = SafeIOUtil.safeCall {
                userAuthUseCase.forgetPasswordRequestOTP(getForgetPasswordRequestBody())
            }
            registerUserResult.onSuccess { result ->
                result?.let {
                    _userForgetPasswordRequestStatus.emit(RequestStatus.Success(result))
                } ?: _userForgetPasswordRequestStatus.emit(RequestStatus.Error(Constants.DEFAULT_ERROR))
            }
            registerUserResult.onFailure { exception ->
                when (exception) {
                    is TripPlannerException -> {
                        _userForgetPasswordRequestStatus.emit(RequestStatus.Error(exception.message))
                    }
                    is Exception -> {
                        _userForgetPasswordRequestStatus.emit(RequestStatus.Error(Constants.DEFAULT_ERROR))
                    }
                }
            }
        }
    }

    private fun updateEmail(email: String) {
        _state.update {
            it.copy(
                email = email,
                isValid = email.isValidEmail()
            )
        }
    }
}