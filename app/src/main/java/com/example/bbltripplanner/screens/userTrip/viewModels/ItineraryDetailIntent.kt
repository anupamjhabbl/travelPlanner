package com.example.bbltripplanner.screens.userTrip.viewModels

import com.example.bbltripplanner.screens.userTrip.entity.AddActivityRequest

sealed class ItineraryDetailIntent {
    sealed class ViewEvent {
        data class FetchActivities(val spotId: String) : ViewEvent()
        data class AddActivity(val spotId: String, val request: AddActivityRequest) : ViewEvent()
        data class UpdateActivity(val activityId: String, val request: AddActivityRequest) : ViewEvent()
        data class DeleteActivity(val activityId: String) : ViewEvent()
    }

    sealed interface ViewEffect {
        data class ErrorInActivityCreation(val message: String): ViewEffect
    }
}
