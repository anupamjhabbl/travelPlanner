package com.example.bbltripplanner.screens.userTrip.viewModels

import com.example.bbltripplanner.screens.userTrip.entity.AddSpotRequest
import com.example.bbltripplanner.screens.userTrip.entity.Location

sealed class ItineraryMapIntent {
    sealed class ViewEvent {
        data class AddSpot(val itineraryId: String, val request: AddSpotRequest) : ViewEvent()
        data class FetchSpots(val itineraryId: String) : ViewEvent()
        data class OnQueryChanged(val query: String) : ViewEvent()
    }

    sealed interface ViewEffect {
        data class ErrorInSpotCreation(val message: String): ViewEffect
        data class ShowSuggestions(val suggestions: List<Location>) : ViewEffect
        object ShowLocationLoading : ViewEffect
        object HideLocationLoading : ViewEffect
    }
}
