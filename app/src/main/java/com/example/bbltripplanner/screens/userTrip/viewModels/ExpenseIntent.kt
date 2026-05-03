package com.example.bbltripplanner.screens.userTrip.viewModels

import com.example.bbltripplanner.screens.userTrip.entity.AddExpenseRequest

sealed class ExpenseIntent {
    sealed class ViewEvent {
        data class FetchExpenses(val tripId: String) : ViewEvent()
        data class InitiateBudget(val tripId: String, val budget: Double) : ViewEvent()
        data class AddExpense(val tripId: String, val request: AddExpenseRequest) : ViewEvent()
        data object FetchSettlements : ViewEvent()
    }

    sealed interface ViewEffect {
        data object AddExpenseLoading: ViewEffect
        data class AddExpenseError(val message: String): ViewEffect
        data object AddExpenseSuccess: ViewEffect
    }
}
