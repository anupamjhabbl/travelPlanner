package com.example.bbltripplanner.screens.userTrip.entity

import com.google.gson.annotations.SerializedName

data class SettlementResponse(
    val overallType: SettlementResponseType,
    val totalSettlementAmount: Double,
    val settlements: List<SettlementItem>
)

enum class SettlementResponseType{
    @SerializedName("RECEIVE")
    RECEIVE,
    @SerializedName("PAY")
    PAY
}