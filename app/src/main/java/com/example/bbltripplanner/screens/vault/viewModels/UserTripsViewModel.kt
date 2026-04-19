package com.example.bbltripplanner.screens.vault.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.vault.usecases.VaultUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserTripsViewModel(private val vaultUseCase: VaultUseCase) : BaseMVIVViewModel<UserTripsViewModelIntent.ViewEvent>() {
    private val _userTripsStatus = MutableStateFlow(RequestResponseStatus<List<TripData>>())
    val userTripsStatus: StateFlow<RequestResponseStatus<List<TripData>>> = _userTripsStatus.asStateFlow()

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

    override fun processEvent(viewEvent: UserTripsViewModelIntent.ViewEvent) {}
}

class UserTripsViewModelIntent {
    sealed interface ViewEvent
}
