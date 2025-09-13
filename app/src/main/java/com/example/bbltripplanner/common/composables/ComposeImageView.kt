package com.example.bbltripplanner.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bbltripplanner.R

object ComposeImageView {
    @Composable
    fun CircularImageView(
        imageURI: String,
        diameter: Dp,
        modifier: Modifier = Modifier,
        borderWidth: Dp = 0.dp,
        borderColor: Color = Color.Transparent,
        contentDescription: String = "",
        onError: Int = R.drawable.ic_default_profile,
        onLoading: Int = R.drawable.ic_default_profile
    ) {
        Box(
            modifier = modifier
                .width(diameter)
                .height(diameter)
                .border(width = borderWidth, color = borderColor, shape = CircleShape)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                model = imageURI,
                placeholder = painterResource(id = onLoading),
                error = painterResource(id = onError),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
        }
    }

    @Composable
    fun ImageViewWitDrawableId(
        imageId: Int,
        modifier: Modifier = Modifier,
        contentDescription: String = ""
    ) {
        Image(
            modifier = modifier
                .padding(0.dp),
            painter = painterResource(id = imageId),
            contentDescription = contentDescription
        )
    }

    @Composable
    fun ImageViewWithUrl(
        imageURI: String,
        modifier: Modifier = Modifier,
        contentDescription: String = "",
        contentScale: ContentScale = ContentScale.Crop
    ) {
        AsyncImage(
            modifier = modifier,
            model = imageURI,
            placeholder = painterResource(id = R.drawable.default_image),
            error = painterResource(id = R.drawable.default_image),
            contentDescription = contentDescription,
            contentScale = contentScale
        )
    }
}