package com.example.bbltripplanner.common.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
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
            dimensionResource(id = R.dimen.module_20sp).toSp()
        },
        textColor: Color = LocalCustomColors.current.titleTextColor,
        lineHeight: TextUnit = TextUnit.Unspecified
    ) {
        val adjustedLineHeight = if (lineHeight == TextUnit.Unspecified && fontSize != TextUnit.Unspecified) {
            (fontSize.value + 4f).sp
        } else {
            lineHeight
        }
        Text(
            text = text,
            modifier = modifier,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.W600,
            textAlign = textAlign,
            fontStyle = CustomTypography.titleLarge.fontStyle,
            fontFamily = primaryFontFamily,
            lineHeight = adjustedLineHeight,
            style = TextStyle.Default
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
        textColor: Color = LocalCustomColors.current.textColor,
        fontWeight: FontWeight = FontWeight.W400,
        maxLines: Int = Int.MAX_VALUE,
        textDecoration: TextDecoration = TextDecoration.None,
        minLines: Int = 1,
        lineHeight: TextUnit = TextUnit.Unspecified
    ) {
        val adjustedLineHeight = if (lineHeight == TextUnit.Unspecified && fontSize != TextUnit.Unspecified) {
            (fontSize.value + 4f).sp
        } else {
            lineHeight
        }
        Text(
            text = text,
            modifier = modifier,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            fontStyle = CustomTypography.titleLarge.fontStyle,
            maxLines = maxLines,
            fontFamily = primaryFontFamily,
            textDecoration = textDecoration,
            minLines = minLines,
            overflow = TextOverflow.Ellipsis,
            lineHeight = adjustedLineHeight,
            style = TextStyle.Default
        )
    }

    private val primaryFontFamily = FontFamily(
        Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
        Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
        Font(R.font.plus_jakarta_sans_light, FontWeight.Light),
        Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium)
    )
}