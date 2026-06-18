package com.example.bbltripplanner.screens.userTrip.entity

import com.google.gson.annotations.SerializedName

data class AddMemberRequest(
    @SerializedName("userId")
    val userId: String
)
