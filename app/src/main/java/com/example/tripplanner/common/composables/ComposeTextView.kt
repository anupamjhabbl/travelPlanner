package com.example.tripplanner.common.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripplanner.ui.theme.CustomTypography
import com.example.tripplanner.ui.theme.LocalCustomColors

object ComposeTextView {
    @Composable
    fun TitleTextView(
        text: String,
        modifier: Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Left,
        fontSize: TextUnit = 18.sp,
        textColor: Color = LocalCustomColors.current.titleTextColor
    ) {
        Text(
            text = text,
            modifier = modifier
                .padding(4.dp, 0.dp),
            color = textColor,
            fontSize = fontSize,
            fontWeight = CustomTypography.titleLarge.fontWeight,
            textAlign = textAlign,
            fontStyle = CustomTypography.titleLarge.fontStyle
        )
    }

    @Composable
    fun TextView(
        text: String,
        modifier: Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Left,
        fontSize: TextUnit = 16.sp,
        textColor: Color = LocalCustomColors.current.textColor,
        fontWeight: FontWeight = CustomTypography.bodyLarge.fontWeight ?: FontWeight.W500
    ) {
        Text(
            text = text,
            modifier = modifier,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            fontStyle = CustomTypography.titleLarge.fontStyle
        )
    }
}