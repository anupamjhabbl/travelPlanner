package com.example.bbltripplanner.screens.userTrip.entity

import androidx.compose.runtime.Stable
import com.example.bbltripplanner.common.entity.User

@Stable
data class ExpenseItem(
    val id: String,
    val description: String,
    val paidBy: User,
    val split: List<User>,
    val amount: Double,
    val date: Long,
    val type: ExpenseType
)