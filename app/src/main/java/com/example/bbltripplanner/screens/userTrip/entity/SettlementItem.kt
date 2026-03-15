package com.example.bbltripplanner.screens.userTrip.entity

import com.example.bbltripplanner.common.entity.User

data class SettlementItem(
    val user: User,
    val amount: Double,
    val isSettled: Boolean,
    val canRequest: Boolean = false
)