package com.example.bbltripplanner.navigation

sealed class HomeNavigationScreen(val route: String) {
    data object MyAccountScreen: HomeNavigationScreen("my_account_screen")
    data object MyProfileScreen: HomeNavigationScreen("my_profile_screen")
}