package com.example.bbltripplanner.screens.home.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bbltripplanner.common.composables.ComposeViewUtils

@Composable
fun TripPlannerSearchScreen(
    navController: NavController
) {
    ComposeViewUtils.PageUnderProgressScreen(
        navController,
        "Search"
    )
}