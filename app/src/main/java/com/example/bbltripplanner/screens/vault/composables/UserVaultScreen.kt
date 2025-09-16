package com.example.bbltripplanner.screens.vault.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bbltripplanner.common.composables.ComposeViewUtils

@Composable
fun UserVaultScreen(
    navController: NavController,
    pageId: String?,
    userId: String?
) {
    ComposeViewUtils.PageUnderProgressScreen(
        navController,
        pageName = "User Vault",
    )
}