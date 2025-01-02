package com.example.bbltripplanner.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

object CommonDrawables {
    @Composable
    fun DotView(
        size: Dp,
        color: Color,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .size(size)
                .background(color, CircleShape)
        )
    }
}