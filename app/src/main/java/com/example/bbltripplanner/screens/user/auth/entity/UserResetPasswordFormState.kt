package com.example.bbltripplanner.screens.user.auth.entity

data class UserResetPasswordFormState(
    val password: String = "",
    val confirmPassword: String = "",
    val passwordValid: PasswordStrengthValidityStatus? = null,
    val confirmPasswordValid: Boolean = true
)

data class UserPasswordResetBody(
    val password: String = ""
)