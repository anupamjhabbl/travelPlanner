package com.example.bbltripplanner.screens.destination.composables

import androidx.compose.runtime.Composable
import com.example.bbltripplanner.common.composables.ComposeViewUtils

@Composable
fun DestinationScreen(
    destinationId: String?
) {
    ComposeViewUtils.PageUnderProgressScreen(
        pageName  = "Destination"
    )
}