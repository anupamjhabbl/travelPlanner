package com.example.bbltripplanner.screens.userTrip.viewModels

import com.example.bbltripplanner.screens.userTrip.entity.AddExpenseRequest

sealed class ExpenseIntent {
    sealed class ViewEvent {
        data class FetchExpenses(val tripId: String) : ViewEvent()
        data class InitiateBudget(val tripId: String, val budget: Double) : ViewEvent()
        data class AddExpense(val tripId: String, val request: AddExpenseRequest) : ViewEvent()
        data object FetchSettlements : ViewEvent()
        data class DeleteExpense(val expenseId: String) : ViewEvent()
    }

    sealed interface AddViewEffect {
        data object AddExpenseLoading: AddViewEffect
        data class AddExpenseError(val message: String): AddViewEffect
        data object AddExpenseSuccess: AddViewEffect
    }

    sealed interface DeleteViewEffect {
        data object DeleteExpenseSuccess: DeleteViewEffect
        data object DeleteExpenseLoading: DeleteViewEffect
        data class DeleteExpenseError(val message: String): DeleteViewEffect
    }
}
