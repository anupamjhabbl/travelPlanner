package com.example.bbltripplanner.screens.user.auth.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.utils.StringUtils.isValidEmail
import com.example.bbltripplanner.screens.user.auth.entity.AuthToken
import com.example.bbltripplanner.screens.user.auth.entity.UserLoginBody
import com.example.bbltripplanner.screens.user.auth.entity.UserLoginFormState
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
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

class UserLoginAuthViewModel(
    private val userAuthUseCase: UserAuthUseCase,
    private val authPreferencesUseCase: AuthPreferencesUseCase
): BaseMVIVViewModel<UserAuthIntent.LoginAuth.ViewEvent>() {
    private val _state: MutableStateFlow<UserLoginFormState> = MutableStateFlow(UserLoginFormState())
    val state: StateFlow<UserLoginFormState> = _state.asStateFlow()

    private val _userLoginRequestStatus: MutableSharedFlow<RequestStatus<String>> = MutableSharedFlow()
    val userLoginRequestStatus: SharedFlow<RequestStatus<String>> = _userLoginRequestStatus.asSharedFlow()

    override fun processEvent(viewEvent: UserAuthIntent.LoginAuth.ViewEvent) {
        when (viewEvent) {
            UserAuthIntent.LoginAuth.ViewEvent.LoginUser -> loginUser()
            is UserAuthIntent.LoginAuth.ViewEvent.UpdateEmail -> updateEmail(viewEvent.email)
            is UserAuthIntent.LoginAuth.ViewEvent.UpdatePassword -> updatePassword(viewEvent.password)
        }
    }

    private fun loginUser() {
        viewModelScope.launch {
            _userLoginRequestStatus.emit(RequestStatus.Loading)
            val registerUserResult = withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    userAuthUseCase.loginUser(getLoginUserBody())
                }
            }
            registerUserResult.onSuccess {  result ->
                saveTheToken(result)
                _userLoginRequestStatus.emit(RequestStatus.Success(""))
            }
            registerUserResult.onFailure { exception ->
                when (exception) {
                    is TripPlannerException -> {
                        _userLoginRequestStatus.emit(RequestStatus.Error(exception.message))
                    }
                    is Exception -> {
                        _userLoginRequestStatus.emit(RequestStatus.Error(Constants.DEFAULT_ERROR))
                    }
                }
            }
        }
    }

    private fun saveTheToken(authToken: AuthToken) {
        authPreferencesUseCase.saveAccessToken(authToken.accessToken)
        authPreferencesUseCase.saveRefreshToken(authToken.refreshToken)
    }

    private fun getLoginUserBody(): UserLoginBody {
        return UserLoginBody(
            email = state.value.email,
            password = state.value.password
        )
    }

    private fun updateEmail(email: String) {
        _state.update {
            it.copy(
                email = email,
                isValid = email.isValidEmail()
            )
        }
    }

    private fun updatePassword(password: String) {
        _state.update {
            it.copy(
                password = password
            )
        }
    }
}