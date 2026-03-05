package com.example.bbltripplanner.screens.userTrip.viewModels

class UserTripDetailIntent {
    sealed interface ViewEvent {
        data class FetchTripDetail(val tripId: String): ViewEvent
        data class AcceptInvitation(val tripId: String): ViewEvent
    }

    sealed interface ViewEffect {
        data class ShowMessage(val isSuccess: Boolean, val message: String? = null): ViewEffect
    }
}