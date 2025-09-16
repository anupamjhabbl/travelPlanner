package com.example.bbltripplanner.screens.notification.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bbltripplanner.common.composables.ComposeViewUtils

@Composable
fun NotificationScreen(
    navController: NavController
) {
    ComposeViewUtils.PageUnderProgressScreen(
        navController,
        pageName  = "Notification"
    )
}