package com.example.bbltripplanner.screens.userTrip.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector
enum class ExpenseType(
    val value: String,
    val icon: ImageVector
) {
    FOOD("Food", Icons.Default.Fastfood),
    TRANSPORT("Transport", Icons.Default.DirectionsBus),
    ACCOMMODATION("Stay", Icons.Default.Hotel),
    SHOPPING("Shopping", Icons.Default.ShoppingBag),
    ACTIVITIES("Activities", Icons.Default.LocalActivity),
    OTHERS("Others", Icons.Default.Payments)
}