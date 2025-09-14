package com.example.bbltripplanner.ui.theme


import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val primaryBackground: Color,
    val secondaryBackground: Color,
    val fadedBackground: Color,
    val titleTextColor: Color,
    val textColor: Color,
    val hintTextColor: Color,
    val primaryButtonBg: Color,
    val primaryButtonText: Color,
    val secondaryButtonText: Color,
    val warning: Color,
    val error: Color,
    val success: Color,
    val defaultImageCardColor: Color,
    val deepPurpleGlow: Color,
    val oppositePrimaryBackground: Color
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        primaryBackground = Color.Unspecified,
        secondaryBackground = Color.Unspecified,
        fadedBackground = Color.Unspecified,
        textColor = Color.Unspecified,
        titleTextColor = Color.Unspecified,
        hintTextColor = Color.Unspecified,
        primaryButtonBg = Color.Unspecified,
        primaryButtonText = Color.Unspecified,
        secondaryButtonText = Color.Unspecified,
        warning = Color.Unspecified,
        error = Color.Unspecified,
        success = Color.Unspecified,
        defaultImageCardColor = Color.Unspecified,
        deepPurpleGlow = Color.Unspecified,
        oppositePrimaryBackground = Color.Unspecified
    )
}

fun getCustomColor(isDarkTheme: Boolean): CustomColors {
    return if (isDarkTheme) {
        CustomColors(
            primaryBackground = DarkColor.primaryBackGround,
            secondaryBackground = DarkColor.secondaryBackground,
            fadedBackground = DarkColor.fadedBackground,
            textColor = DarkColor.textColor,
            titleTextColor = DarkColor.titleTextColor,
            hintTextColor = DarkColor.hintTextColor,
            primaryButtonText = DarkColor.primaryButtonText,
            primaryButtonBg = DarkColor.primaryButtonBg,
            secondaryButtonText = DarkColor.secondaryButtonText,
            warning = DarkColor.warning,
            error = DarkColor.error,
            success = DarkColor.success,
            defaultImageCardColor = DarkColor.defaultImageCardColor,
            deepPurpleGlow = DarkColor.deepPurpleGlow,
            oppositePrimaryBackground = LightColor.primaryBackGround
        )
    } else {
        CustomColors(
            primaryBackground = LightColor.primaryBackGround,
            secondaryBackground = LightColor.secondaryBackground,
            fadedBackground = LightColor.fadedBackground,
            textColor = LightColor.textColor,
            titleTextColor = LightColor.titleTextColor,
            hintTextColor = LightColor.hintTextColor,
            primaryButtonText = LightColor.primaryButtonText,
            primaryButtonBg = LightColor.primaryButtonBg,
            secondaryButtonText = LightColor.secondaryButtonText,
            warning = DarkColor.warning,
            error = DarkColor.error,
            success = DarkColor.success,
            defaultImageCardColor = LightColor.defaultImageCardColor,
            deepPurpleGlow = LightColor.lightPurpleGlow,
            oppositePrimaryBackground = DarkColor.primaryBackGround
        )
    }
}

