package com.example.bbltripplanner.screens.notification.viewModels

class NotificationViewModelIntent {
    sealed interface ViewEvent {
        data object FetchNotification: ViewEvent
    }
}