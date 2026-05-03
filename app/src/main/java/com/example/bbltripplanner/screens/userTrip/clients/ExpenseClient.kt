package com.example.bbltripplanner.screens.userTrip.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.AddExpenseRequest
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseItem
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseSummary
import com.example.bbltripplanner.screens.userTrip.entity.InitiateExpenseRequest
import com.example.bbltripplanner.screens.userTrip.entity.SettlementResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExpenseClient {
    @GET("trips/{tripId}/expenses")
    suspend fun getExpenses(@Path("tripId") tripId: String): Response<BaseResponse<ExpenseSummary>>

    @POST("trips/{tripId}/initiateExpense")
    suspend fun initiateExpense(
        @Path("tripId") tripId: String,
        @Body request: InitiateExpenseRequest
    ): Response<BaseResponse<ExpenseSummary>>

    @POST("trips/{tripId}/expenses")
    suspend fun addExpense(
        @Path("tripId") tripId: String,
        @Body expenseRequest: AddExpenseRequest
    ): Response<BaseResponse<ExpenseItem>>

    @GET("trips/{tripId}/settlements")
    suspend fun getSettlements(@Path("tripId") tripId: String): Response<BaseResponse<SettlementResponse>>
}