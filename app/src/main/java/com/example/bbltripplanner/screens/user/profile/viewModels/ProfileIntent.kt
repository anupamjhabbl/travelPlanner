package com.example.bbltripplanner.screens.user.profile.viewModels

class ProfileIntent {
    sealed interface ViewState {
        data object FollowSuccess: ViewState
        data object FollowFailure: ViewState
        data object BlockSuccess: ViewState
        data object BlockFailure: ViewState
    }

    sealed interface ViewEvent {
        data class SetUp(val userId: String): ViewEvent
        data object FollowUser: ViewEvent
        data object BlockUser : ViewEvent
    }
}