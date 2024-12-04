package com.example.tripplanner.ui.theme


import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val primaryBackground: Color,
    val titleTextColor: Color,
    val textColor: Color,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val secondaryTextColor: Color,
    val highlightTextColor: Color,
    val primaryButtonBg: Color,
    val secondaryButtonBorder: Color,
    val primaryButtonText: Color,
    val secondaryButtonText: Color,
    val warning: Color,
    val error: Color,
    val success: Color
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        primaryBackground = Color.Unspecified,
        textColor = Color.Unspecified,
        titleTextColor = Color.Unspecified,
        primaryAccent = Color.Unspecified,
        secondaryAccent = Color.Unspecified,
        secondaryTextColor = Color.Unspecified,
        highlightTextColor = Color.Unspecified,
        primaryButtonBg = Color.Unspecified,
        secondaryButtonBorder = Color.Unspecified,
        primaryButtonText = Color.Unspecified,
        secondaryButtonText = Color.Unspecified,
        warning = Color.Unspecified,
        error = Color.Unspecified,
        success = Color.Unspecified
    )
}

fun getCustomColor(isDarkTheme: Boolean): CustomColors {
    return if (isDarkTheme) {
        CustomColors(
            primaryBackground = DarkColor.primaryBackGround,
            textColor = DarkColor.textColor,
            titleTextColor = DarkColor.titleTextColor,
            primaryAccent = DarkColor.primaryAccent,
            secondaryAccent = DarkColor.secondaryAccent,
            secondaryTextColor = DarkColor.secondaryTextColor,
            highlightTextColor = DarkColor.highlightTextColor,
            primaryButtonText = DarkColor.primaryButtonText,
            primaryButtonBg = DarkColor.primaryButtonBg,
            secondaryButtonBorder = DarkColor.secondaryButtonBorder,
            secondaryButtonText = DarkColor.secondaryButtonText,
            warning = DarkColor.warning,
            error = DarkColor.error,
            success = DarkColor.success
        )
    } else {
        CustomColors(
            primaryBackground = LightColor.primaryBackGround,
            textColor = LightColor.textColor,
            titleTextColor = LightColor.titleTextColor,
            primaryAccent = LightColor.primaryAccent,
            secondaryAccent = LightColor.secondaryAccent,
            secondaryTextColor = LightColor.secondaryTextColor,
            highlightTextColor = LightColor.highlightTextColor,
            primaryButtonText = LightColor.primaryButtonText,
            primaryButtonBg = LightColor.primaryButtonBg,
            secondaryButtonBorder = LightColor.secondaryButtonBorder,
            secondaryButtonText = LightColor.secondaryButtonText,
            warning = DarkColor.warning,
            error = DarkColor.error,
            success = DarkColor.success
        )
    }
}

