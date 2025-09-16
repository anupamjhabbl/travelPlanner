package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.usecases.UserTripDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserTripDetailViewModel(
    private val userTripDetailUseCase: UserTripDetailUseCase
): BaseMVIVViewModel<UserTripDetailIntent.ViewEvent>() {
    private val _userTripDetailFetchStatus: MutableStateFlow<RequestResponseStatus<TripData>> = MutableStateFlow(RequestResponseStatus())
    val userTripDetailFetchStatus: StateFlow<RequestResponseStatus<TripData>> = _userTripDetailFetchStatus.asStateFlow()

    override fun processEvent(viewEvent: UserTripDetailIntent.ViewEvent) {
        when (viewEvent) {
            is UserTripDetailIntent.ViewEvent.FetchTripDetail -> fetchTripDetail(viewEvent.tripId)
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
}