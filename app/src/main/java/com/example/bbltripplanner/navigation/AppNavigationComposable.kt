package com.example.bbltripplanner.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.BottomNavigationPanel
import com.example.bbltripplanner.common.entity.BottomNavigationItem
import com.example.bbltripplanner.screens.buzz.composables.BuzzScreen
import com.example.bbltripplanner.screens.home.composables.HomeExperienceScreen
import com.example.bbltripplanner.screens.notification.composables.NotificationScreen
import com.example.bbltripplanner.screens.posting.composables.PostingInitScreen
import com.example.bbltripplanner.screens.posting.composables.UserTripDetailScreen
import com.example.bbltripplanner.screens.user.auth.composables.AuthenticationFormScreen
import com.example.bbltripplanner.screens.user.auth.composables.ForgotPasswordScreen
import com.example.bbltripplanner.screens.user.auth.composables.OTPVerificationScreen
import com.example.bbltripplanner.screens.user.auth.composables.PasswordResetScreen
import com.example.bbltripplanner.screens.user.general.composables.HelpSupportScreen
import com.example.bbltripplanner.screens.user.general.composables.UserSettingsScreen
import com.example.bbltripplanner.screens.user.myacount.composables.MyAccountView
import com.example.bbltripplanner.screens.user.profile.composables.EditProfileScreen
import com.example.bbltripplanner.screens.user.profile.composables.ProfileSocialScreen
import com.example.bbltripplanner.screens.user.profile.composables.ProfileScreen
import com.example.bbltripplanner.screens.vault.composables.UserVaultScreen
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun AppNavigationComposable(
    accessToken: String
) {
    val homeNavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomNavigationItems = getNavigationItemList()
    val navStackElements by homeNavController.visibleEntries.collectAsState()
    val selectedTabIndex = remember { mutableIntStateOf(0) }
    val navBackStackEntry = navStackElements.lastOrNull()?.destination
    val currentRoute = navBackStackEntry?.route
    val parentRoute = navBackStackEntry?.parent?.route
    for (index in bottomNavigationItems.indices) {
        if (bottomNavigationItems[index].route == (parentRoute ?: currentRoute)) {
            if (selectedTabIndex.intValue != index) {
                selectedTabIndex.intValue = index
            }
        }
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.primaryBackground),
        bottomBar = {
            if (navBackStackEntry.toAppNavigationScreen()?.hasBottomBar == true) {
                BottomNavigationPanel(
                    homeNavController,
                    selectedTabIndex.intValue,
                    bottomNavigationItems
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Box(
            modifier = Modifier
                .background(LocalCustomColors.current.primaryBackground)
                .padding(padding)
        ) {
            HomeNavigationComposable(accessToken, homeNavController)
        }
    }
}

@Composable
fun HomeNavigationComposable(
    accessToken: String,
    homeNavController: NavHostController
) {
    val startDestination = if (accessToken.isEmpty()){
        AppNavigationScreen.AuthGraph.route
    } else  {
        AppNavigationScreen.HomeScreen.route
    }
    NavHost(
        navController = homeNavController,
        startDestination = startDestination
    ) {
        navigation(
            route = AppNavigationScreen.UserScreenGraph.route,
            startDestination = AppNavigationScreen.AccountScreen.route
        ) {
            composable(route = AppNavigationScreen.AccountScreen.route) {
                MyAccountView(homeNavController)
            }

            composable(route = AppNavigationScreen.ProfileScreen.route) { navBackStackEntry ->
                val userId =  navBackStackEntry.arguments?.getString(Constants.NavigationArgs.USER_ID)
                ProfileScreen(homeNavController, userId)
            }

            composable(route = AppNavigationScreen.HelpSupportScreen.route) {
                HelpSupportScreen(homeNavController)
            }

            composable(route = AppNavigationScreen.UserSettingsScreen.route) {
                UserSettingsScreen(homeNavController)
            }

            composable(route = AppNavigationScreen.ProfileSocialScreen.route) { navBackStackEntry ->
                val pageId = navBackStackEntry.arguments?.getString(Constants.NavigationArgs.PAGE_ID)
                val userId = navBackStackEntry.arguments?.getString(Constants.NavigationArgs.USER_ID)
                ProfileSocialScreen(homeNavController, pageId, userId)
            }

            composable(route = AppNavigationScreen.EditProfileScreen.route) {
                EditProfileScreen(homeNavController)
            }
        }

        navigation(
            route = AppNavigationScreen.AuthGraph.route,
            startDestination = AppNavigationScreen.AuthenticationFormScreen.route
        ) {
            composable(route = AppNavigationScreen.AuthenticationFormScreen.route) {
                AuthenticationFormScreen(homeNavController)
            }

            composable(route = AppNavigationScreen.ForgotPasswordScreen.route) {
                ForgotPasswordScreen(homeNavController)
            }

            composable(route = AppNavigationScreen.ResetPasswordScreen.route) {
                PasswordResetScreen(homeNavController)
            }

            composable(route = AppNavigationScreen.OtpVerificationScreen.route) { navBackStackEntry ->
                val userEmail =  navBackStackEntry.arguments?.getString(Constants.NavigationArgs.USER_EMAIL)
                val origin = navBackStackEntry.arguments?.getString(Constants.NavigationArgs.ORIGIN)
                val userId = navBackStackEntry.arguments?.getString(Constants.NavigationArgs.USER_ID)
                OTPVerificationScreen(homeNavController, userEmail, origin, userId)
            }
        }

        composable(route = AppNavigationScreen.HomeScreen.route) {
            HomeExperienceScreen(homeNavController)
        }

        composable(route = AppNavigationScreen.NotificationScreen.route) {
            NotificationScreen(homeNavController)
        }

        composable(route = AppNavigationScreen.VaultScreen.route) { navBackStackEntry ->
            val pageId = navBackStackEntry.arguments?.getString(Constants.NavigationArgs.PAGE_ID)
            val userId = navBackStackEntry.arguments?.getString(Constants.NavigationArgs.USER_ID)
            UserVaultScreen(homeNavController, pageId, userId)
        }

        composable(route = AppNavigationScreen.AddScreen.route) {
            PostingInitScreen(homeNavController)
        }

        composable(route = AppNavigationScreen.BuzzScreen.route) {
            BuzzScreen(homeNavController)
        }

        composable(route = AppNavigationScreen.UserTripDetailScreen.route) { navBackStackEntry ->
            val tripId = navBackStackEntry.arguments?.getString(Constants.NavigationArgs.TRIP_ID)
            UserTripDetailScreen(homeNavController, tripId)
        }
    }
}

fun getNavigationItemList(): List<BottomNavigationItem> {
    val titleList = listOf(Constants.BottomNavigationItem.HOME, Constants.BottomNavigationItem.VAULT, Constants.BottomNavigationItem.ADD, Constants.BottomNavigationItem.BUZZ, Constants.BottomNavigationItem.PROFILE)
    val routeList = listOf(Constants.NavigationScreen.HOME_SCREEN, Constants.NavigationScreen.VAULT_SCREEN, Constants.NavigationScreen.ADD_SCREEN, Constants.NavigationScreen.BUZZ_SCREEN, Constants.NavigationScreen.USER_SCREEN_GRAPH)
    val outlineIconDrawable = listOf(R.drawable.ic_home_outline, R.drawable.ic_vault_outline, R.drawable.ic_add_filled, R.drawable.ic_buzz_outline, R.drawable.ic_profile_outline)
    val filledIconDrawable = listOf(R.drawable.ic_home_filled, R.drawable.ic_vault_filled, R.drawable.ic_add_filled, R.drawable.ic_buzz_filled, R.drawable.ic_profile_filled)
    val navigationItemList = mutableListOf<BottomNavigationItem>()
    for (i in 0..< minOf(titleList.size, routeList.size, outlineIconDrawable.size, filledIconDrawable.size)) {
        navigationItemList.add(
            BottomNavigationItem(titleList[i], routeList[i], filledIconDrawable[i], outlineIconDrawable[i])
        )
    }
    return navigationItemList
}