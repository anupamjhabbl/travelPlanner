package com.example.bbltripplanner.screens.user.auth.entity

data class UserLoginFormState(
    val email: String = "",
    val password: String = "",
    val isValid: Boolean = false
)

data class UserLoginBody(
    val email: String = "",
    val password: String = ""
)
