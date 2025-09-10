package com.example.bbltripplanner.common.composables

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.ui.theme.LocalCustomColors

object ComposeButtonView {
    @Composable
    fun IconButtonView (
        modifier: Modifier,
        iconDrawable: Int
    ) {
        IconButton(
            modifier = modifier,
            onClick = {}
        ) {
            ComposeImageView.ImageViewWitDrawableId(
                imageId = iconDrawable,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    
    @Composable
    fun PrimaryButtonView (
        modifier: Modifier = Modifier,
        backgroundColor: Color = LocalCustomColors.current.secondaryBackground,
        contentColor: Color = LocalCustomColors.current.primaryBackground,
        text: String,
        shape: Shape = RoundedCornerShape(8.dp),
        paddingValues: PaddingValues = PaddingValues(16.dp, 8.dp),
        fontSize: TextUnit = 12.sp,
        fontWeight: FontWeight = FontWeight.SemiBold,
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick,
            colors = ButtonColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContentColor = backgroundColor,
                disabledContainerColor = contentColor
            ),
            modifier = modifier
                .defaultMinSize(minWidth = 0.dp, minHeight = 0.dp)
                .height(IntrinsicSize.Min),
            shape = shape,
            contentPadding = paddingValues
        ) {
            ComposeTextView.TextView(
                modifier = Modifier.wrapContentWidth().padding(0.dp),
                text = text,
                textColor = contentColor,
                fontSize = fontSize,
                fontWeight = fontWeight
            )
        }
    }
}