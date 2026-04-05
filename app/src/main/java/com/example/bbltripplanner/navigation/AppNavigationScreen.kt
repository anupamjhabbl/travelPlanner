package com.example.bbltripplanner.navigation

import androidx.navigation.NavDestination
import com.example.bbltripplanner.common.Constants

sealed class AppNavigationScreen(
    val route: String,
    val hasBottomBar: Boolean = false,
    val isFullScreen: Boolean = false
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
    data object EditTripScreen: AppNavigationScreen(route = "${Constants.NavigationScreen.EDIT_TRIP_SCREEN}/{${Constants.NavigationArgs.TRIP_ID}}") {
        fun createRoute(tripId: String) = "${Constants.NavigationScreen.EDIT_TRIP_SCREEN}/$tripId"
    }
    data object ExpenseScreen: AppNavigationScreen(route = "${Constants.NavigationScreen.EXPENSE_SCREEN}/{${Constants.NavigationArgs.TRIP_ID}}") {
        fun createRoute(tripId: String) = "${Constants.NavigationScreen.EXPENSE_SCREEN}/$tripId"
    }
    data object AddExpenseScreen: AppNavigationScreen(route = "${Constants.NavigationScreen.ADD_EXPENSE_SCREEN}/{${Constants.NavigationArgs.TRIP_ID}}") {
        fun createRoute(tripId: String) = "${Constants.NavigationScreen.ADD_EXPENSE_SCREEN}/$tripId"
    }
    data object ExpenseSettlementScreen: AppNavigationScreen(route = "${Constants.NavigationScreen.EXPENSE_SETTLEMENT_SCREEN}/{${Constants.NavigationArgs.TRIP_ID}}") {
        fun createRoute(tripId: String) = "${Constants.NavigationScreen.EXPENSE_SETTLEMENT_SCREEN}/$tripId"
    }
    data object ItineraryNavEntry: AppNavigationScreen(route = Constants.NavigationScreen.ITINERARY_NAV_ENTRY)
    data object ItineraryMapViewScreen: AppNavigationScreen(route = "${Constants.NavigationScreen.ITINERARY_MAP_VIEW_SCREEN}/{${Constants.NavigationArgs.TRIP_SELECTED_DATE}}", isFullScreen = true) {
        fun createRoute(tripSelectedDate: String) = "${Constants.NavigationScreen.ITINERARY_MAP_VIEW_SCREEN}/$tripSelectedDate"
    }
    data object ItineraryListView: AppNavigationScreen(route = "${Constants.NavigationScreen.ITINERARY_LIST_VIEW}/{${Constants.NavigationArgs.TRIP_ID}}", isFullScreen = true) {
        fun createRoute(tripId: String) = "${Constants.NavigationScreen.ITINERARY_LIST_VIEW}/$tripId"
    }
    data object ItineraryDetailView: AppNavigationScreen(route = "${Constants.NavigationScreen.ITINERARY_DETAIL_VIEW}/{${Constants.NavigationArgs.ITINERARY_PLACE_ID}}", isFullScreen = true) {
        fun createRoute(placeId: String) = "${Constants.NavigationScreen.ITINERARY_DETAIL_VIEW}/$placeId"
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
    return when {
        route.startsWith(Constants.NavigationScreen.ACCOUNT_SCREEN) -> AppNavigationScreen.AccountScreen
        route.startsWith(Constants.NavigationScreen.PROFILE_SCREEN) -> AppNavigationScreen.ProfileScreen
        route.startsWith(Constants.NavigationScreen.USER_SCREEN_GRAPH) -> AppNavigationScreen.UserScreenGraph
        route.startsWith(Constants.NavigationScreen.EDIT_PROFILE_SCREEN) -> AppNavigationScreen.EditProfileScreen
        route.startsWith(Constants.NavigationScreen.USER_SETTING_SCREEN) -> AppNavigationScreen.UserSettingsScreen
        route.startsWith(Constants.NavigationScreen.HELP_SUPPORT_SCREEN) -> AppNavigationScreen.HelpSupportScreen
        route.startsWith(Constants.NavigationScreen.PROFILE_SOCIAL_SCREEN) -> AppNavigationScreen.ProfileSocialScreen

        // Home Screens
        route.startsWith(Constants.NavigationScreen.HOME_NAV_GRAPH) -> AppNavigationScreen.HomeNavGraph
        route.startsWith(Constants.NavigationScreen.HOME_SCREEN) -> AppNavigationScreen.HomeScreen
        route.startsWith(Constants.NavigationScreen.SEARCH_SCREEN) -> AppNavigationScreen.SearchScreen

        // General
        route.startsWith(Constants.NavigationScreen.VAULT_SCREEN) -> AppNavigationScreen.VaultScreen
        route.startsWith(Constants.NavigationScreen.BUZZ_SCREEN) -> AppNavigationScreen.BuzzScreen
        route.startsWith(Constants.NavigationScreen.NOTIFICATION_SCREEN) -> AppNavigationScreen.NotificationScreen

        // User Trip And Posting Screens
        route.startsWith(Constants.NavigationScreen.ADD_SCREEN) -> AppNavigationScreen.AddScreen
        route.startsWith(Constants.NavigationScreen.USER_TRIP_DETAIL_SCREEN) -> AppNavigationScreen.UserTripDetailScreen
        route.startsWith(Constants.NavigationScreen.EDIT_TRIP_SCREEN) -> AppNavigationScreen.EditTripScreen
        route.startsWith(Constants.NavigationScreen.EXPENSE_SCREEN) -> AppNavigationScreen.ExpenseScreen
        route.startsWith(Constants.NavigationScreen.ADD_EXPENSE_SCREEN) -> AppNavigationScreen.AddExpenseScreen
        route.startsWith(Constants.NavigationScreen.EXPENSE_SETTLEMENT_SCREEN) -> AppNavigationScreen.ExpenseSettlementScreen
        route.startsWith(Constants.NavigationScreen.ITINERARY_MAP_VIEW_SCREEN) -> AppNavigationScreen.ItineraryMapViewScreen

        // Authentication
        route.startsWith(Constants.NavigationScreen.AUTH_GRAPH) -> AppNavigationScreen.AuthGraph
        route.startsWith(Constants.NavigationScreen.AUTHENTICATION_FORM_SCREEN) -> AppNavigationScreen.AuthenticationFormScreen
        route.startsWith(Constants.NavigationScreen.RESET_PASSWORD_SCREEN) -> AppNavigationScreen.ResetPasswordScreen
        route.startsWith(Constants.NavigationScreen.FORGOT_PASSWORD_SCREEN) -> AppNavigationScreen.ForgotPasswordScreen
        route.startsWith(Constants.NavigationScreen.OTP_VERIFICATION_SCREEN) -> AppNavigationScreen.OtpVerificationScreen

        // Destination screen
        route.startsWith(Constants.NavigationScreen.DESTINATION_SCREEN) -> AppNavigationScreen.DestinationScreen
        else -> null
    }
}
