package com.example.bbltripplanner.screens.userTrip.viewModels

import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.screens.userTrip.entity.TripCreationResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripVisibility

class PostingInitIntent {
    sealed interface ViewEvent {
        data object SaveAndContinue: ViewEvent
        data object UpdateAndContinue: ViewEvent
        data class OnQueryChanged(val query: String): ViewEvent
        data class UpdateStartDate(val startDate: Long): ViewEvent
        data class UpdateEndDate(val endDate: Long): ViewEvent
        data class UpdateTripLocation(val location: Location): ViewEvent
        data class UpdateTripName(val tripName: String): ViewEvent
        data class SetTripVisibility(val tripVisibility: TripVisibility): ViewEvent
        data class AddTripMates(val user: User): ViewEvent
        data object GetInviteList: ViewEvent
        data class RemoveTripMates(val user: User): ViewEvent
        data class GetTripDetails(val tripId: String): ViewEvent
    }

    sealed interface ViewEffect  {
        data class GoNext(val tripData: TripCreationResponse): ViewEffect
        data object ShowError: ViewEffect
        data class ShowSuggestions(val suggestions: List<Location>): ViewEffect
        data class ShowFullScreenError(val message: String): ViewEffect
        data object ShowSuccess: ViewEffect
        data object ShowLoading: ViewEffect
        data object HideLoading: ViewEffect
        data object ShowFollowersLoading: ViewEffect
        data object HideFollowersLoading: ViewEffect
        data object ShowLocationLoading: ViewEffect
        data object HideLocationLoading: ViewEffect
    }
}