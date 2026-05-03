package com.example.bbltripplanner.screens.userTrip.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.annotations.SerializedName

enum class ExpenseType(
    val value: String,
    val icon: ImageVector
) {
    @SerializedName("FOOD")
    FOOD("Food", Icons.Default.Fastfood),
    @SerializedName("TRANSPORT")
    TRANSPORT("Transport", Icons.Default.DirectionsBus),
    @SerializedName("ACCOMMODATION")
    ACCOMMODATION("STAY", Icons.Default.Hotel),
    @SerializedName("SHOPPING")
    SHOPPING("SHOPPING", Icons.Default.ShoppingBag),
    @SerializedName("Activities")
    ACTIVITIES("ACTIVITIES", Icons.Default.LocalActivity),
    @SerializedName("OTHERS")
    OTHERS("Others", Icons.Default.Payments)
}