package com.example.bbltripplanner.navigation

import com.example.bbltripplanner.common.Constants

sealed class AppNavigationScreen(val route: String, val hasBottomBar: Boolean) {
    data object AccountScreen: AppNavigationScreen(route = Constants.NavigationScreen.ACCOUNT_SCREEN, hasBottomBar = false)
    data object HomeScreen: AppNavigationScreen(route = Constants.NavigationScreen.HOME_SCREEN, hasBottomBar = true)
    data object VaultScreen: AppNavigationScreen(route = Constants.NavigationScreen.VAULT_SCREEN, hasBottomBar = true)
    data object AddScreen: AppNavigationScreen(route = Constants.NavigationScreen.ADD_SCREEN, hasBottomBar = true)
    data object BuzzScreen: AppNavigationScreen(route = Constants.NavigationScreen.BUZZ_SCREEN, hasBottomBar = true)
    data object ProfileScreen: AppNavigationScreen(route = Constants.NavigationScreen.PROFILE_SCREEN, hasBottomBar = true)
    data object UserScreenGraph: AppNavigationScreen(route = Constants.NavigationScreen.USER_SCREEN_GRAPH, hasBottomBar = true)
}