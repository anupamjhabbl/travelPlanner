package com.example.bbltripplanner.main.viewModels

class MainActivityIntent {
    sealed interface ViewEvent {
        data object Init: ViewEvent
    }
}