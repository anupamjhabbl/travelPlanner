package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun AddSpotsScreen(
    tripId: String?,
    date: Long?,
) {
    val customColors = LocalCustomColors.current
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground),
        contentAlignment = Alignment.Center
    ) {
        ComposeTextView.TitleTextView(
            text = stringResource(id = R.string.add_spots_title),
            fontSize = 24.sp,
            textColor = customColors.titleTextColor
        )
        // Implementation for adding spots will go here
    }
}
