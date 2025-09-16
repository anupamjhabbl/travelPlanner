package com.example.bbltripplanner.screens.buzz.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bbltripplanner.common.composables.ComposeViewUtils

@Composable
fun BuzzScreen(
    navController: NavController
) {
    ComposeViewUtils.PageUnderProgressScreen(
        navController,
        pageName = "Buzz"
    )
}