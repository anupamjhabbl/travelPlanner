package com.example.bbltripplanner.navigation

import androidx.navigation.NavDestination
import com.example.bbltripplanner.common.Constants

sealed class AppNavigationScreen(val route: String, val hasBottomBar: Boolean) {
    // User Auth Screens
    data object AuthGraph: AppNavigationScreen(Constants.NavigationScreen.AUTH_GRAPH, false)
    data object AuthenticationFormScreen: AppNavigationScreen(Constants.NavigationScreen.AUTHENTICATION_FORM_SCREEN, false)
    data object ResetPasswordScreen: AppNavigationScreen(Constants.NavigationScreen.RESET_PASSWORD_SCREEN, false)
    data object ForgotPasswordScreen: AppNavigationScreen(Constants.NavigationScreen.FORGOT_PASSWORD_SCREEN,  false)
    data object OtpVerificationScreen: AppNavigationScreen(
        route = "${Constants.NavigationScreen.OTP_VERIFICATION_SCREEN}?" +
                "${Constants.NavigationArgs.USER_EMAIL}={${Constants.NavigationArgs.USER_EMAIL}}&" +
                "${Constants.NavigationArgs.USER_ID}={${Constants.NavigationArgs.USER_ID}}&" +
                "${Constants.NavigationArgs.ORIGIN}={${Constants.NavigationArgs.ORIGIN}}",
        hasBottomBar = false
    ) {
        fun createRoute(userEmail: String, origin: String, userId: String) =
            "${Constants.NavigationScreen.OTP_VERIFICATION_SCREEN}?" +
                    "${Constants.NavigationArgs.USER_EMAIL}=$userEmail&" +
                    "${Constants.NavigationArgs.USER_ID}=$userId&" +
                    "${Constants.NavigationArgs.ORIGIN}=$origin"
    }

    // User Home Screens
    data object HomeScreen: AppNavigationScreen(route = Constants.NavigationScreen.HOME_SCREEN, hasBottomBar = true)

    // User Screens Graph
    data object UserScreenGraph: AppNavigationScreen(route = Constants.NavigationScreen.USER_SCREEN_GRAPH, hasBottomBar = true)
    data object ProfileScreen: AppNavigationScreen(route = Constants.NavigationScreen.PROFILE_SCREEN, hasBottomBar = false)
    data object AccountScreen: AppNavigationScreen(route = Constants.NavigationScreen.ACCOUNT_SCREEN, hasBottomBar = true)

    // User Trip And Posting Screens
    data object AddScreen: AppNavigationScreen(route = Constants.NavigationScreen.ADD_SCREEN, hasBottomBar = false)
    data object UserTripDetailScreen: AppNavigationScreen(route = "${Constants.NavigationScreen.USER_TRIP_DETAIL_SCREEN}/{${Constants.NavigationArgs.TRIP_ID}}", hasBottomBar = false) {
        fun createRoute(tripId: String) = "${Constants.NavigationScreen.USER_TRIP_DETAIL_SCREEN}/$tripId"
    }

    // General
    data object VaultScreen: AppNavigationScreen(route = Constants.NavigationScreen.VAULT_SCREEN, hasBottomBar = true)
    data object BuzzScreen: AppNavigationScreen(route = Constants.NavigationScreen.BUZZ_SCREEN, hasBottomBar = true)
}

fun NavDestination?.toAppNavigationScreen(): AppNavigationScreen? {
    val route = this?.route ?: return null
    return when (route) {
        // Profile
        Constants.NavigationScreen.ACCOUNT_SCREEN -> AppNavigationScreen.AccountScreen
        Constants.NavigationScreen.PROFILE_SCREEN -> AppNavigationScreen.ProfileScreen
        Constants.NavigationScreen.USER_SCREEN_GRAPH -> AppNavigationScreen.UserScreenGraph

        // User
        Constants.NavigationScreen.HOME_SCREEN -> AppNavigationScreen.HomeScreen

        // General
        Constants.NavigationScreen.VAULT_SCREEN -> AppNavigationScreen.VaultScreen
        Constants.NavigationScreen.BUZZ_SCREEN -> AppNavigationScreen.BuzzScreen

        // Trip & Posting
        Constants.NavigationScreen.ADD_SCREEN -> AppNavigationScreen.AddScreen
        Constants.NavigationScreen.USER_TRIP_DETAIL_SCREEN -> AppNavigationScreen.UserTripDetailScreen

        // Authentication
        Constants.NavigationScreen.AUTH_GRAPH -> AppNavigationScreen.AuthGraph
        Constants.NavigationScreen.AUTHENTICATION_FORM_SCREEN -> AppNavigationScreen.AuthenticationFormScreen
        Constants.NavigationScreen.RESET_PASSWORD_SCREEN -> AppNavigationScreen.ResetPasswordScreen
        Constants.NavigationScreen.FORGOT_PASSWORD_SCREEN -> AppNavigationScreen.ForgotPasswordScreen
        Constants.NavigationScreen.OTP_VERIFICATION_SCREEN -> AppNavigationScreen.OtpVerificationScreen
        else -> null
    }
}