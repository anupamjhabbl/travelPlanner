package com.example.bbltripplanner.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.bbltripplanner.screens.posting.composables.PostingInitScreen
import com.example.bbltripplanner.screens.user.myacount.composables.MyAccountView
import com.example.bbltripplanner.screens.user.profile.composables.MyProfileView
import com.example.bbltripplanner.screens.vault.composables.UserVaultScreen
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun AppNavigationComposable() {
    val homeNavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomNavigationItems = getNavigationItemList()
    val navStackElements by homeNavController.visibleEntries.collectAsState()
    val selectedTabIndex = remember { mutableIntStateOf(0) }
    for (index in bottomNavigationItems.indices) {
        val navBackStackEntry = navStackElements.lastOrNull()?.destination
        val currentRoute = navBackStackEntry?.route
        val parentRoute = navBackStackEntry?.parent?.route
        if (bottomNavigationItems[index].route == (parentRoute ?: currentRoute)) {
            selectedTabIndex.intValue = index
        }
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.primaryBackground),
        bottomBar = {
            BottomNavigationPanel(homeNavController, selectedTabIndex.intValue, bottomNavigationItems)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        HomeNavigationComposable(homeNavController, snackbarHostState, padding)
    }
}

@Composable
fun HomeNavigationComposable(
    homeNavController: NavHostController,
    snackbarHostState: SnackbarHostState,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = homeNavController,
        startDestination = AppNavigationScreen.HomeScreen.route
    ) {
        navigation(
            route = AppNavigationScreen.UserScreenGraph.route,
            startDestination = AppNavigationScreen.AccountScreen.route
        ) {
            composable(route = AppNavigationScreen.AccountScreen.route) {
                MyAccountView(homeNavController)
            }

            composable(route = AppNavigationScreen.ProfileScreen.route) {
                MyProfileView(homeNavController)
            }
        }

        composable(route = AppNavigationScreen.HomeScreen.route) {
            HomeExperienceScreen(homeNavController)
        }

        composable(route = AppNavigationScreen.VaultScreen.route) {
            UserVaultScreen(homeNavController)
        }

        composable(route = AppNavigationScreen.AddScreen.route) {
            PostingInitScreen(homeNavController)
        }

        composable(route = AppNavigationScreen.BuzzScreen.route) {
            BuzzScreen(homeNavController)
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