package com.example.bbltripplanner.screens.user.profile.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bbltripplanner.common.composables.ComposeViewUtils

@Composable
fun EditProfileScreen(
    navController: NavController
) {
    ComposeViewUtils.FullScreenErrorComposable(
        errorStrings = Pair("Work Under Progress", "Edit profile Page will get live soon")
    )
}