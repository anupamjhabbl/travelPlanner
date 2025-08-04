package com.example.bbltripplanner.screens.vault.composables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun UserVaultScreen(
    navController: NavController
) {
    Text(
        "Hello world! Vault Screen",
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .wrapContentSize()
    )
}