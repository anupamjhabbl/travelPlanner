package com.example.bbltripplanner.common.utils

import android.net.Uri
import android.util.Patterns
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.screens.user.auth.entity.PasswordStrengthValidityStatus

object StringUtils {
    fun String.isValidEmail(): Boolean {
        return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun String.isStrongPassword(): PasswordStrengthValidityStatus {
        return if (this.length < 8) {
            PasswordStrengthValidityStatus.SMALL_LENGTH
        } else if (!this.any { it.isUpperCase() }) {
            PasswordStrengthValidityStatus.UPPER_CASE_MISSING
        } else if (!this.any { it.isLowerCase() }) {
            PasswordStrengthValidityStatus.LOWER_CASE_MISSING
        } else if (!this.any { it.isDigit() }) {
            PasswordStrengthValidityStatus.DIGIT_MISSING
        } else if (!this.any { "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~".contains(it) }) {
            PasswordStrengthValidityStatus.SPECIAL_CHARACTER_MISSING
        } else {
            PasswordStrengthValidityStatus.VALID
        }
    }

    fun getDeeplinkForUserShare(
        userId: String
    ): String {
        return Uri.Builder()
            .scheme("https")
            .authority(Constants.APP_NAME_URI)
            .appendPath(AppNavigationScreen.ProfileScreen.route)
            .appendPath(userId)
            .build()
            .toString()
    }
}