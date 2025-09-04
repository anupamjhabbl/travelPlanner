package com.example.bbltripplanner.navigation

import androidx.navigation.NavDestination
import com.example.bbltripplanner.common.Constants

sealed class AppNavigationScreen(val route: String, val hasBottomBar: Boolean) {
    data object AccountScreen: AppNavigationScreen(route = Constants.NavigationScreen.ACCOUNT_SCREEN, hasBottomBar = false)
    data object HomeScreen: AppNavigationScreen(route = Constants.NavigationScreen.HOME_SCREEN, hasBottomBar = true)
    data object VaultScreen: AppNavigationScreen(route = Constants.NavigationScreen.VAULT_SCREEN, hasBottomBar = true)
    data object AddScreen: AppNavigationScreen(route = Constants.NavigationScreen.ADD_SCREEN, hasBottomBar = false)
    data object BuzzScreen: AppNavigationScreen(route = Constants.NavigationScreen.BUZZ_SCREEN, hasBottomBar = true)
    data object ProfileScreen: AppNavigationScreen(route = Constants.NavigationScreen.PROFILE_SCREEN, hasBottomBar = true)
    data object UserScreenGraph: AppNavigationScreen(route = Constants.NavigationScreen.USER_SCREEN_GRAPH, hasBottomBar = true)
    data object UserTripDetailScreen: AppNavigationScreen(route = "${Constants.NavigationScreen.USER_TRIP_DETAIL_SCREEN}/{${Constants.NavigationArgs.TRIP_ID}}", hasBottomBar = false) {
        fun createRoute(tripId: String) = "${Constants.NavigationScreen.USER_TRIP_DETAIL_SCREEN}/$tripId"
    }
}

fun NavDestination?.toAppNavigationScreen(): AppNavigationScreen? {
    val route = this?.route ?: return null
    return when (route) {
        Constants.NavigationScreen.ACCOUNT_SCREEN -> AppNavigationScreen.AccountScreen
        Constants.NavigationScreen.HOME_SCREEN -> AppNavigationScreen.HomeScreen
        Constants.NavigationScreen.VAULT_SCREEN -> AppNavigationScreen.VaultScreen
        Constants.NavigationScreen.ADD_SCREEN -> AppNavigationScreen.AddScreen
        Constants.NavigationScreen.BUZZ_SCREEN -> AppNavigationScreen.BuzzScreen
        Constants.NavigationScreen.PROFILE_SCREEN -> AppNavigationScreen.ProfileScreen
        Constants.NavigationScreen.USER_SCREEN_GRAPH -> AppNavigationScreen.UserScreenGraph
        Constants.NavigationScreen.USER_TRIP_DETAIL_SCREEN -> AppNavigationScreen.UserTripDetailScreen
        else -> null
    }
}