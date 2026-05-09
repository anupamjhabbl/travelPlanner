package com.example.bbltripplanner.screens.userTrip.repositoryImpl

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.clients.ExpenseClient
import com.example.bbltripplanner.screens.userTrip.entity.AddExpenseRequest
import com.example.bbltripplanner.screens.userTrip.entity.Currency
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseItem
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseSummary
import com.example.bbltripplanner.screens.userTrip.entity.InitiateExpenseRequest
import com.example.bbltripplanner.screens.userTrip.entity.SettlementResponse
import com.example.bbltripplanner.screens.userTrip.repositories.ExpenseRepository

class ExpenseNetwork(
    private val expenseClient: ExpenseClient
) : ExpenseRepository {

    override suspend fun getExpenses(tripId: String): ExpenseSummary? {
        return BaseResponse.processResponse { expenseClient.getExpenses(tripId) }
    }

    override suspend fun initiateExpense(tripId: String, budget: Double, currency: Currency): ExpenseSummary? {
        return BaseResponse.processResponse { expenseClient.initiateExpense(tripId, InitiateExpenseRequest(budget, currency)) }
    }

    override suspend fun addExpense(
        tripId: String,
        request: AddExpenseRequest
    ): ExpenseItem? {
        return BaseResponse.processResponse { expenseClient.addExpense(tripId, request) }
    }


    override suspend fun getSettlements(tripId: String): SettlementResponse? {
        return BaseResponse.processResponse { expenseClient.getSettlements(tripId) }!!
    }

    override suspend fun deleteExpense(expenseId: String): String? {
        return BaseResponse.processResponse { expenseClient.deleteExpense(expenseId) }
    }
}
