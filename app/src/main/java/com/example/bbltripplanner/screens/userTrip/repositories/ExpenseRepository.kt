package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.screens.userTrip.entity.AddExpenseRequest
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseItem
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseSummary
import com.example.bbltripplanner.screens.userTrip.entity.SettlementResponse

interface ExpenseRepository {
    suspend fun getExpenses(tripId: String): ExpenseSummary?
    suspend fun initiateExpense(tripId: String, budget: Double): ExpenseSummary?
    suspend fun addExpense(tripId: String, request: AddExpenseRequest): ExpenseItem?
    suspend fun getSettlements(tripId: String): SettlementResponse?
}