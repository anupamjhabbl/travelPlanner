package com.example.bbltripplanner.screens.buzz.composables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun BuzzScreen(
    navController: NavController
) {
    Text(
        "Hello world! Buzz screen",
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .wrapContentSize()
    )
}