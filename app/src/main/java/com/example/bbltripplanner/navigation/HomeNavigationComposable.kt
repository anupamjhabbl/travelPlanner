package com.example.bbltripplanner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bbltripplanner.user.myacount.composables.MyAccountView
import com.example.bbltripplanner.user.profile.composables.MyProfileView

@Composable
fun HomeNavigationComposable() {
    val homeNavController = rememberNavController()

    NavHost(navController = homeNavController, startDestination = HomeNavigationScreen.MyAccountScreen.route) {
        composable(route = HomeNavigationScreen.MyAccountScreen.route) {
            MyAccountView(homeNavController)
        }

        composable(route = HomeNavigationScreen.MyProfileScreen.route) {
            MyProfileView(homeNavController)
        }
    }
}