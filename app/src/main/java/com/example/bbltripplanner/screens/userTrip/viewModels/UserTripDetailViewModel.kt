package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.usecases.UserTripDetailUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class UserTripDetailViewModel(
    private val userTripDetailUseCase: UserTripDetailUseCase,
    private val authPreferencesUseCase: AuthPreferencesUseCase
): BaseMVIVViewModel<UserTripDetailIntent.ViewEvent>() {
    private val _userTripDetailFetchStatus: MutableStateFlow<RequestResponseStatus<TripData>> = MutableStateFlow(RequestResponseStatus())
    val userTripDetailFetchStatus: StateFlow<RequestResponseStatus<TripData>> = _userTripDetailFetchStatus.asStateFlow()

    private val _viewEffect: Channel<UserTripDetailIntent.ViewEffect> = Channel()
    val viewEffect = _viewEffect.receiveAsFlow()

    override fun processEvent(viewEvent: UserTripDetailIntent.ViewEvent) {
        when (viewEvent) {
            is UserTripDetailIntent.ViewEvent.FetchTripDetail -> fetchTripDetail(viewEvent.tripId)
            is UserTripDetailIntent.ViewEvent.AcceptInvitation -> acceptInvitation(viewEvent.tripId)
        }
    }

    private fun acceptInvitation(tripId: String) {
        _userTripDetailFetchStatus.value = userTripDetailFetchStatus.value.copy(isLoading = true)
        viewModelScope.launch {
            val tripDetailResult = SafeIOUtil.safeCall {
                userTripDetailUseCase.acceptInvitation(tripId)
            }
            tripDetailResult.onSuccess {
                _userTripDetailFetchStatus.value = userTripDetailFetchStatus.value.copy(isLoading = false)
                _viewEffect.send(UserTripDetailIntent.ViewEffect.ShowMessage(true))
            }
            tripDetailResult.onFailure {
                _userTripDetailFetchStatus.value = userTripDetailFetchStatus.value.copy(isLoading = false)
                _viewEffect.send(UserTripDetailIntent.ViewEffect.ShowMessage(false, it.message))
            }
        }
    }

    private fun fetchTripDetail(tripId: String) {
        _userTripDetailFetchStatus.value = userTripDetailFetchStatus.value.copy(isLoading = true)
        viewModelScope.launch {
            val tripDetailResult = SafeIOUtil.safeCall {
                userTripDetailUseCase.getUserTripDetail(tripId)
            }
            tripDetailResult.onSuccess { tripData ->
                _userTripDetailFetchStatus.value =  userTripDetailFetchStatus.value.copy(isLoading = false, data = tripData)
            }
            tripDetailResult.onFailure {
                _userTripDetailFetchStatus.value = userTripDetailFetchStatus.value.copy(isLoading = false, error = it.message)
            }
        }
    }

    fun getUserId(): String? {
        return authPreferencesUseCase.getUserIdLogged()
    }
}