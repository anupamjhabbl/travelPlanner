package com.example.bbltripplanner.screens.vault.composables

import androidx.compose.runtime.Composable
import com.example.bbltripplanner.common.composables.ComposeViewUtils

@Composable
fun UserVaultScreen(
    pageId: String?,
    userId: String?
) {
    ComposeViewUtils.PageUnderProgressScreen(
        pageName = "User Vault",
    )
}