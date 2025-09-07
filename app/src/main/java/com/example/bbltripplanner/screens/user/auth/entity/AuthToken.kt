package com.example.bbltripplanner.screens.user.auth.entity

import com.google.gson.annotations.SerializedName

data class AuthToken(
    @SerializedName("accessToken")
    val accessToken: String = "",
    @SerializedName("refreshToken")
    val refreshToken: String = ""
)

data class UserRegisteredId(
    @SerializedName("userId")
    val userId: String
)

data class PasswordResetResponse(
    val message: String
)