package com.example.bbltripplanner.screens.user.auth.entity

import com.google.gson.annotations.SerializedName

data class OTPState(
    val otpSize: Int,
    val code: List<Int?> = (1..otpSize).map { null },
    val focusedIndex: Int? = null,
    val isValid: Boolean = false
)

data class UserOTPVerifyBody(
    @SerializedName("userId")
    val userId: String = "",
    val otp: String = ""
)