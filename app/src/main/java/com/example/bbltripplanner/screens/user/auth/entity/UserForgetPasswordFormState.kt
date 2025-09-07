package com.example.bbltripplanner.screens.user.auth.entity

data class UserForgetPasswordFormState(
    val email: String = "",
    val isValid: Boolean = false
)

data class UserForgetPasswordBody(
    val email: String = ""
)