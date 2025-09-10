package com.example.bbltripplanner.screens.posting.viewModels

class UserTripDetailIntent {
    sealed interface ViewEvent {
        data class FetchTripDetail(val tripId: String): ViewEvent
    }
}