package com.example.bbltripplanner.screens.vault.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.vault.usecases.VaultUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserTripsViewModel(private val vaultUseCase: VaultUseCase) : BaseMVIVViewModel<UserTripsViewModelIntent.ViewEvent>() {
    private val _userTripsStatus = MutableStateFlow(RequestResponseStatus<List<TripData>>())
    val userTripsStatus: StateFlow<RequestResponseStatus<List<TripData>>> = _userTripsStatus.asStateFlow()

    private val _deleteTripStatus = MutableSharedFlow<UserTripsViewModelIntent.DeleteViewEffect>()
    val deleteTripStatus: SharedFlow<UserTripsViewModelIntent.DeleteViewEffect> = _deleteTripStatus.asSharedFlow()

    init {
        getUserTrips()
    }

    fun getUserTrips() {
        viewModelScope.launch {
            _userTripsStatus.value = _userTripsStatus.value.copy(isLoading = true)
            try {
                val trips = vaultUseCase.getUserTrips()
                _userTripsStatus.value = _userTripsStatus.value.copy(
                    isLoading = false,
                    data = trips,
                    error = null
                )
            } catch (e: Exception) {
                _userTripsStatus.value = _userTripsStatus.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun deleteTrip(tripId: String) {
        viewModelScope.launch {
            _deleteTripStatus.emit(UserTripsViewModelIntent.DeleteViewEffect.DeleteTripLoading)
            try {
                vaultUseCase.deleteTrip(tripId)
                _deleteTripStatus.emit(UserTripsViewModelIntent.DeleteViewEffect.DeleteTripSuccess)

                val currentList = _userTripsStatus.value.data
                if (currentList != null) {
                    _userTripsStatus.value = _userTripsStatus.value.copy(
                        data = currentList.filter { it.tripId != tripId }
                    )
                }
            } catch (e: Exception) {
                _deleteTripStatus.emit(UserTripsViewModelIntent.DeleteViewEffect.DeleteTripError(e.message ?: "Failed to delete trip"))
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
