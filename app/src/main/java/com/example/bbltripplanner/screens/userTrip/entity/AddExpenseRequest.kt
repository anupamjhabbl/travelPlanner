package com.example.bbltripplanner.screens.userTrip.entity

data class AddExpenseRequest(
    val description: String,
    val paidById: String,
    val splitUserIds: List<String>,
    val amount: Double,
    val type: ExpenseType
)