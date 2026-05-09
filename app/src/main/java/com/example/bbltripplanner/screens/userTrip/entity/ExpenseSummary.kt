package com.example.bbltripplanner.screens.userTrip.entity

data class ExpenseSummary(
    val budget: Double,
    val currencyCode: Currency,
    val expenses: List<ExpenseItem>
)