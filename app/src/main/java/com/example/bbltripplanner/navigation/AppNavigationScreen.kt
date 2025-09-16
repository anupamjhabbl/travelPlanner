package com.example.bbltripplanner.navigation

import androidx.navigation.NavDestination
import com.example.bbltripplanner.common.Constants

sealed class AppNavigationScreen(
    val route: String,
    val hasBottomBar: Boolean = false
) {
    // User Auth Screens
    data object AuthGraph: AppNavigationScreen(Constants.NavigationScreen.AUTH_GRAPH)
    data object AuthenticationFormScreen: AppNavigationScreen(Constants.NavigationScreen.AUTHENTICATION_FORM_SCREEN)
    data object ResetPasswordScreen: AppNavigationScreen(Constants.NavigationScreen.RESET_PASSWORD_SCREEN)
    data object ForgotPasswordScreen: AppNavigationScreen(Constants.NavigationScreen.FORGOT_PASSWORD_SCREEN)
    data object OtpVerificationScreen: AppNavigationScreen(
        route = "${Constants.NavigationScreen.OTP_VERIFICATION_SCREEN}?" +
                "${Constants.NavigationArgs.USER_EMAIL}={${Constants.NavigationArgs.USER_EMAIL}}&" +
                "${Constants.NavigationArgs.USER_ID}={${Constants.NavigationArgs.USER_ID}}&" +
                "${Constants.NavigationArgs.ORIGIN}={${Constants.NavigationArgs.ORIGIN}}"
    ) {
        fun createRoute(userEmail: String, origin: String, userId: String) =
            "${Constants.NavigationScreen.OTP_VERIFICATION_SCREEN}?" +
                    "${Constants.NavigationArgs.USER_EMAIL}=$userEmail&" +
                    "${Constants.NavigationArgs.USER_ID}=$userId&" +
                    "${Constants.NavigationArgs.ORIGIN}=$origin"
    }

    // Home Screens
    data  object HomeNavGraph: AppNavigationScreen(route = Constants.NavigationScreen.HOME_NAV_GRAPH, hasBottomBar = true)
    data object HomeScreen: AppNavigationScreen(route = Constants.NavigationScreen.HOME_SCREEN, hasBottomBar = true)
    data object SearchScreen: AppNavigationScreen(route = Constants.NavigationScreen.SEARCH_SCREEN)

    // User Screens
    data object UserScreenGraph: AppNavigationScreen(route = Constants.NavigationScreen.USER_SCREEN_GRAPH, hasBottomBar = true)
    data object AccountScreen: AppNavigationScreen(route = Constants.NavigationScreen.ACCOUNT_SCREEN, hasBottomBar = true)
    data object EditProfileScreen: AppNavigationScreen(route = Constants.NavigationScreen.EDIT_PROFILE_SCREEN)
    data object UserSettingsScreen: AppNavigationScreen(route = Constants.NavigationScreen.USER_SETTING_SCREEN)
    data object HelpSupportScreen: AppNavigationScreen(route = Constants.NavigationScreen.HELP_SUPPORT_SCREEN)
    data object ProfileSocialScreen: AppNavigationScreen(
        route = "${Constants.NavigationScreen.PROFILE_SOCIAL_SCREEN}?" +
                "${Constants.NavigationArgs.PAGE_ID}={${Constants.NavigationArgs.PAGE_ID}}&" +
                "${Constants.NavigationArgs.USER_ID}={${Constants.NavigationArgs.USER_ID}}&"
    ) {
        fun createRoute(pageId: String, userId: String) =
            "${Constants.NavigationScreen.PROFILE_SOCIAL_SCREEN}?" +
                    "${Constants.NavigationArgs.PAGE_ID}=$pageId&" +
                    "${Constants.NavigationArgs.USER_ID}=$userId"
    }
    data object ProfileScreen: AppNavigationScreen(route = "${Constants.NavigationScreen.PROFILE_SCREEN}/{${Constants.NavigationArgs.USER_ID}}") {
        fun createRoute(userId: String) ="${Constants.NavigationScreen.PROFILE_SCREEN}/$userId"
    }

    // User Trip And Posting Screens
    data object AddScreen: AppNavigationScreen(route = Constants.NavigationScreen.ADD_SCREEN)
    data object UserTripDetailScreen: AppNavigationScreen(route = "${Constants.NavigationScreen.USER_TRIP_DETAIL_SCREEN}/{${Constants.NavigationArgs.TRIP_ID}}") {
        fun createRoute(tripId: String) = "${Constants.NavigationScreen.USER_TRIP_DETAIL_SCREEN}/$tripId"
    }

    // General
    data object BuzzScreen: AppNavigationScreen(route = Constants.NavigationScreen.BUZZ_SCREEN, hasBottomBar = true)
    data object NotificationScreen: AppNavigationScreen(route = Constants.NavigationScreen.NOTIFICATION_SCREEN)
    data object VaultScreen: AppNavigationScreen(
        route = "${Constants.NavigationScreen.VAULT_SCREEN}?" +
                "${Constants.NavigationArgs.PAGE_ID}={${Constants.NavigationArgs.PAGE_ID}}&" +
                "${Constants.NavigationArgs.USER_ID}={${Constants.NavigationArgs.USER_ID}}&"
    ) {
        fun createRoute(pageId: String, userId: String) =
            "${Constants.NavigationScreen.VAULT_SCREEN}?" +
                    "${Constants.NavigationArgs.PAGE_ID}=$pageId&" +
                    "${Constants.NavigationArgs.USER_ID}=$userId"
    }

    // destination
    data object DestinationScreen: AppNavigationScreen(route = "${Constants.NavigationScreen.DESTINATION_SCREEN}/{${Constants.NavigationArgs.DESTINATION_ID}}") {
        fun createRoute(destinationId: String) = "${Constants.NavigationScreen.DESTINATION_SCREEN}/$destinationId"
    }
}

fun NavDestination?.toAppNavigationScreen(): AppNavigationScreen? {
    val route = this?.route ?: return null
    return when (route) {
        // User Screens
        Constants.NavigationScreen.ACCOUNT_SCREEN -> AppNavigationScreen.AccountScreen
        Constants.NavigationScreen.PROFILE_SCREEN -> AppNavigationScreen.ProfileScreen
        Constants.NavigationScreen.USER_SCREEN_GRAPH -> AppNavigationScreen.UserScreenGraph
        Constants.NavigationScreen.EDIT_PROFILE_SCREEN -> AppNavigationScreen.EditProfileScreen
        Constants.NavigationScreen.USER_SETTING_SCREEN -> AppNavigationScreen.UserSettingsScreen
        Constants.NavigationScreen.HELP_SUPPORT_SCREEN -> AppNavigationScreen.HelpSupportScreen
        Constants.NavigationScreen.PROFILE_SOCIAL_SCREEN -> AppNavigationScreen.ProfileSocialScreen

        // Home Screens
        Constants.NavigationScreen.HOME_NAV_GRAPH -> AppNavigationScreen.HomeNavGraph
        Constants.NavigationScreen.HOME_SCREEN -> AppNavigationScreen.HomeScreen
        Constants.NavigationScreen.SEARCH_SCREEN -> AppNavigationScreen.SearchScreen

        // General
        Constants.NavigationScreen.VAULT_SCREEN -> AppNavigationScreen.VaultScreen
        Constants.NavigationScreen.BUZZ_SCREEN -> AppNavigationScreen.BuzzScreen
        Constants.NavigationScreen.NOTIFICATION_SCREEN -> AppNavigationScreen.NotificationScreen

        // User Trip And Posting Screens
        Constants.NavigationScreen.ADD_SCREEN -> AppNavigationScreen.AddScreen
        Constants.NavigationScreen.USER_TRIP_DETAIL_SCREEN -> AppNavigationScreen.UserTripDetailScreen

        // Authentication
        Constants.NavigationScreen.AUTH_GRAPH -> AppNavigationScreen.AuthGraph
        Constants.NavigationScreen.AUTHENTICATION_FORM_SCREEN -> AppNavigationScreen.AuthenticationFormScreen
        Constants.NavigationScreen.RESET_PASSWORD_SCREEN -> AppNavigationScreen.ResetPasswordScreen
        Constants.NavigationScreen.FORGOT_PASSWORD_SCREEN -> AppNavigationScreen.ForgotPasswordScreen
        Constants.NavigationScreen.OTP_VERIFICATION_SCREEN -> AppNavigationScreen.OtpVerificationScreen

        // Destination screen
        Constants.NavigationScreen.DESTINATION_SCREEN -> AppNavigationScreen.DestinationScreen
        else -> null
    }
}