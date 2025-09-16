package com.example.bbltripplanner.screens.user.profile.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bbltripplanner.common.composables.ComposeViewUtils

@Composable
fun ProfileSocialScreen(
    navController: NavController,
    pageId: String?,
    userId: String?
) {
    ComposeViewUtils.PageUnderProgressScreen(
        navController,
        pageName = "User Social"
    )
}