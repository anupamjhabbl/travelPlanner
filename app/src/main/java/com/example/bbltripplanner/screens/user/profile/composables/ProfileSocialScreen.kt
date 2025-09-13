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
    ComposeViewUtils.FullScreenErrorComposable(
        errorStrings = Pair("Work Under Progress", "Social Page will get live soon $pageId $userId")
    )
}