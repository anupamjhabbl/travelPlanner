package com.example.bbltripplanner.screens.userTrip.viewModels

import com.example.bbltripplanner.screens.userTrip.entity.TripData

class PostingInitIntent {
    sealed interface ViewEvent {
        data object SaveAndContinue: ViewEvent
    }

    sealed interface ViewEffect  {
        data class GoNext(val tripData: TripData): ViewEffect
        data object ShowError: ViewEffect
    }
}