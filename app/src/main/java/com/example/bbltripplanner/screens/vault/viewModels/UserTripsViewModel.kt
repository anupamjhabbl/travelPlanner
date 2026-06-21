package com.example.bbltripplanner.screens.vault.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.vault.usecases.VaultUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserTripsViewModel(private val vaultUseCase: VaultUseCase) : BaseMVIVViewModel<UserTripsViewModelIntent.ViewEvent>() {
    private val _userTripsStatus = MutableStateFlow(RequestResponseStatus<List<TripData>>())
    val userTripsStatus: StateFlow<RequestResponseStatus<List<TripData>>> = _userTripsStatus.asStateFlow()

    private val _deleteTripStatus = MutableSharedFlow<UserTripsViewModelIntent.DeleteViewEffect>()
    val deleteTripStatus: SharedFlow<UserTripsViewModelIntent.DeleteViewEffect> = _deleteTripStatus.asSharedFlow()

    init {
        getUserTrips()
    }

    fun getUserTrips() {
        _userTripsStatus.value = _userTripsStatus.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = SafeIOUtil.safeCall {
                vaultUseCase.getUserTrips()
            }
            result.onSuccess { trips ->
                _userTripsStatus.value = _userTripsStatus.value.copy(
                    isLoading = false,
                    data = trips,
                    error = null
                )
            }
            result.onFailure { exception ->
                val errorMsg = when {
                    exception is java.io.IOException -> Constants.ErrorType.NETWORK_ERROR
                    exception is HttpException && exception.code() == 404 -> Constants.ErrorType.NOT_FOUND
                    exception is HttpException && exception.code() == 403 -> Constants.ErrorType.NOT_AUTHORIZED
                    exception is com.example.bbltripplanner.common.entity.TripPlannerException && exception.errorCode in 500..599 -> Constants.ErrorType.SERVER_ERROR
                    exception is com.example.bbltripplanner.common.entity.TripPlannerException -> exception.message ?: Constants.ErrorType.SERVER_ERROR
                    else -> Constants.ErrorType.SERVER_ERROR
                }
                _userTripsStatus.value = _userTripsStatus.value.copy(
                    isLoading = false,
                    error = errorMsg
                )
            }
        }
    }

    private fun deleteTrip(tripId: String) {
        viewModelScope.launch {
            _deleteTripStatus.emit(UserTripsViewModelIntent.DeleteViewEffect.DeleteTripLoading)
            val result = SafeIOUtil.safeCall {
                vaultUseCase.deleteTrip(tripId)
            }
            result.onSuccess {
                _deleteTripStatus.emit(UserTripsViewModelIntent.DeleteViewEffect.DeleteTripSuccess)
                val currentList = _userTripsStatus.value.data
                if (currentList != null) {
                    _userTripsStatus.value = _userTripsStatus.value.copy(
                        data = currentList.filter { it.tripId != tripId }
                    )
                }
            }
            result.onFailure { exception ->
                val errorMsg = when {
                    exception is java.io.IOException -> Constants.ErrorType.NETWORK_ERROR
                    exception is HttpException && exception.code() == 404 -> Constants.ErrorType.NOT_FOUND
                    exception is HttpException && exception.code() == 403 -> Constants.ErrorType.NOT_AUTHORIZED
                    exception is com.example.bbltripplanner.common.entity.TripPlannerException && exception.errorCode in 500..599 -> Constants.ErrorType.SERVER_ERROR
                    exception is com.example.bbltripplanner.common.entity.TripPlannerException -> exception.message ?: Constants.ErrorType.SERVER_ERROR
                    else -> Constants.ErrorType.SERVER_ERROR
                }
                _deleteTripStatus.emit(UserTripsViewModelIntent.DeleteViewEffect.DeleteTripError(errorMsg))
            }
        }
    }

    override fun processEvent(viewEvent: UserTripsViewModelIntent.ViewEvent) {
        when (viewEvent) {
            is UserTripsViewModelIntent.ViewEvent.DeleteTrip -> deleteTrip(viewEvent.tripId)
        }
    }
}

class UserTripsViewModelIntent {
    sealed interface ViewEvent {
        data class DeleteTrip(val tripId: String) : ViewEvent
    }

    sealed interface DeleteViewEffect {
        data object DeleteTripLoading : DeleteViewEffect
        data object DeleteTripSuccess : DeleteViewEffect
        data class DeleteTripError(val message: String) : DeleteViewEffect
    }
}
