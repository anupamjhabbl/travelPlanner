package com.example.bbltripplanner.screens.userTrip.entity

data class TripExpensesDetail(
    val budget: Double,
    val currency: Currency,
    val expense: Double,
    val left: Double,
    val expenses: List<ExpenseItem>
)