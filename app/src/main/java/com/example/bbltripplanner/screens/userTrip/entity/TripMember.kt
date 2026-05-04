package com.example.bbltripplanner.screens.userTrip.entity

import com.example.bbltripplanner.common.entity.User
import com.google.gson.annotations.SerializedName

data class TripMember(
    @SerializedName("user")
    val user: User,
    @SerializedName("status")
    val status: TripMemberStatus
)

enum class TripMemberStatus(val value: String) {
    @SerializedName("pending")
    PENDING("pending"),
    @SerializedName("accepted")
    ACCEPTED("accepted")
}
