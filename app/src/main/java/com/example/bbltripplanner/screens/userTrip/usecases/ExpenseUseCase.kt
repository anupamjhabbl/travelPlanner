package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.entity.AddExpenseRequest
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseItem
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseSummary
import com.example.bbltripplanner.screens.userTrip.entity.SettlementResponse
import com.example.bbltripplanner.screens.userTrip.repositories.ExpenseRepository

class ExpenseUseCase(private val repository: ExpenseRepository) {
    suspend fun getExpenses(tripId: String): ExpenseSummary? {
        return repository.getExpenses(tripId)
    }

    suspend fun initiateExpense(tripId: String, budget: Double): ExpenseSummary? {
        return repository.initiateExpense(tripId, budget)
    }

    suspend fun addExpense(tripId: String, request: AddExpenseRequest): ExpenseItem? {
        return repository.addExpense(tripId, request)
    }

    suspend fun getSettlements(tripId: String): SettlementResponse? {
        return repository.getSettlements(tripId)
    }

    suspend fun deleteExpense(expenseId: String): String? {
        return repository.deleteExpense(expenseId)
    }
}
