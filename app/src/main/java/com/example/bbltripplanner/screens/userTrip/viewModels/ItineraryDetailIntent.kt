package com.example.bbltripplanner.screens.userTrip.viewModels

sealed class ItineraryDetailIntent {
    sealed class ViewEvent {
        data class FetchPlaceDetail(val placeId: String) : ViewEvent()
    }
}
