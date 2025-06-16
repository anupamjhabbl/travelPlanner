package com.example.bbltripplanner.common.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.example.bbltripplanner.R
import com.example.bbltripplanner.ui.theme.CustomTypography
import com.example.bbltripplanner.ui.theme.LocalCustomColors

object ComposeTextView {
    @Composable
    fun TitleTextView(
        text: String,
        modifier: Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Left,
        fontSize: TextUnit = with(LocalDensity.current) {
            dimensionResource(id = R.dimen.module_18sp).toSp()
        },
        textColor: Color = LocalCustomColors.current.titleTextColor
    ) {
        Text(
            text = text,
            modifier = modifier,
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
        fontSize: TextUnit = with(LocalDensity.current) {
            dimensionResource(id = R.dimen.module_16sp).toSp()
        },
        textColor: Color = LocalCustomColors.current.textColor,
        fontWeight: FontWeight = CustomTypography.bodyLarge.fontWeight ?: FontWeight.W500,
        maxLines: Int = Int.MAX_VALUE
    ) {
        Text(
            text = text,
            modifier = modifier,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            fontStyle = CustomTypography.titleLarge.fontStyle,
            maxLines = maxLines
        )
    }
}