package com.example.bbltripplanner.screens.userTrip.viewModels

class UserTripDetailIntent {
    sealed interface ViewEvent {
        data class FetchTripDetail(val tripId: String): ViewEvent
    }
}