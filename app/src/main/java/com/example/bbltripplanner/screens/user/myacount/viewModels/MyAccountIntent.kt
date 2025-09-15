package com.example.bbltripplanner.screens.user.myacount.viewModels

class MyAccountIntent {
    sealed interface ViewState {
        data object Loading: ViewState
        data object LogoutSuccess: ViewState
        data object LogoutFailure: ViewState
    }

    sealed interface ViewEvent {
        data object LogoutUser: ViewEvent
    }
}