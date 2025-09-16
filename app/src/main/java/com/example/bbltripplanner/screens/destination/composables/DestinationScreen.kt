package com.example.bbltripplanner.screens.destination.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bbltripplanner.common.composables.ComposeViewUtils

@Composable
fun DestinationScreen(
    navController: NavController,
    destinationId: String?
) {
    ComposeViewUtils.PageUnderProgressScreen(
        navController,
        pageName  = "Destination"
    )
}