package com.example.bbltripplanner.screens.user.general.viewModels

interface HelpSupportViewModelIntent {
    sealed interface ViewEvent {
        data class PerformSearch(val faqCategories: List<FAQCategory>, val searchQuery: String): ViewEvent
        data class ToggleCategory(val category: Int): ViewEvent
    }
}