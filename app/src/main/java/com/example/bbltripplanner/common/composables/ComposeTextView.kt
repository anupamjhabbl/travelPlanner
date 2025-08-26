package com.example.bbltripplanner.common.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.example.bbltripplanner.R
import com.example.bbltripplanner.ui.theme.CustomTypography

object ComposeTextView {
    @Composable
    fun TitleTextView(
        text: String,
        modifier: Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Left,
        fontSize: TextUnit = with(LocalDensity.current) {
            dimensionResource(id = R.dimen.module_20sp).toSp()
        },
        textColor: Color = colorResource(R.color.textPrimary)
    ) {
        Text(
            text = text,
            modifier = modifier,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.W600,
            textAlign = textAlign,
            fontStyle = CustomTypography.titleLarge.fontStyle,
            fontFamily = primaryFontFamily
        )
    }

    @Composable
    fun TextView(
        text: String,
        modifier: Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Left,
        fontSize: TextUnit = with(LocalDensity.current) {
            dimensionResource(id = R.dimen.module_12sp).toSp()
        },
        textColor: Color = colorResource(R.color.textSecondary),
        fontWeight: FontWeight = FontWeight.W400,
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
            maxLines = maxLines,
            fontFamily = primaryFontFamily
        )
    }

    val primaryFontFamily = FontFamily(
        Font(R.font.lato_regular, FontWeight.Normal),
        Font(R.font.lato_bold, FontWeight.Bold),
        Font(R.font.lato_light, FontWeight.Light)
    )
}