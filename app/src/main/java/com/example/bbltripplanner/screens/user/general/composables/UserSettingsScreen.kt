package com.example.bbltripplanner.screens.user.general.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bbltripplanner.common.composables.ComposeViewUtils

@Composable
fun UserSettingsScreen(
    navController: NavController
) {
    ComposeViewUtils.PageUnderProgressScreen(
        navController,
        pageName = "Settings"
    )
}