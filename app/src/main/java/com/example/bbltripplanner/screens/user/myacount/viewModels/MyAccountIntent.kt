package com.example.bbltripplanner.screens.user.myacount.viewModels

class MyAccountIntent {
    sealed interface ViewState {
        data object Loading: ViewState
        data object LogoutSuccess: ViewState
        data class LogoutFailure(val errorType: String?): ViewState
    }

    sealed interface ViewEvent {
        data object LogoutUser: ViewEvent
    }
}