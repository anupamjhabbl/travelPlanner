package com.example.bbltripplanner.screens.home.composables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.bbltripplanner.screens.home.viewModels.HomeExperienceViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeExperienceScreen(
    navController: NavController
) {
    val viewModel: HomeExperienceViewModel = koinViewModel()

    Text(
        "Hello world! Home screen",
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .wrapContentSize()
    )
}