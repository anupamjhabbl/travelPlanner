package com.example.bbltripplanner.screens.home.viewModels

import com.example.bbltripplanner.screens.home.entities.CxeResponseError

class HomeExperienceIntent {
    sealed interface ViewEffect {
        data object GoToLocationFeedScreen: ViewEffect
    }

    sealed interface ViewState {
        data object ShowFullScreenLoading: ViewState
        data class ShowCxeResponseError(val error: CxeResponseError): ViewState
    }

    sealed interface ViewEvent {
        data object Initialize: ViewEvent
    }
}