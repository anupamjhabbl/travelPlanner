package com.example.bbltripplanner.screens.userTrip.viewModels

sealed class ItineraryIntent {
    sealed class ViewEvent {
        data class FetchItinerary(val tripId: String) : ViewEvent()
        data class GenerateItinerary(val tripId: String) : ViewEvent()
    }
}
