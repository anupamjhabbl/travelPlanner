package com.example.bbltripplanner.common.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.example.bbltripplanner.R

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
        backgroundColor: Color,
        text: String,
        textColor: Color,
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.padding(
                dimensionResource(id = R.dimen.module_24),
                dimensionResource(id = R.dimen.module_8),
                dimensionResource(id = R.dimen.module_24),
                dimensionResource(id = R.dimen.module_8)
            ),
            colors = ButtonColors(
                containerColor = backgroundColor,
                contentColor = textColor,
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            ),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.module_8))
        ) {
            ComposeTextView.TextView(
                text = text,
                textColor = textColor
            )
        }
    }
}