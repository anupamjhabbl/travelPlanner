package com.example.tripplanner.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tripplanner.R
import com.example.tripplanner.common.composables.ComposeImageView.ImageViewWitDrawable
import com.example.tripplanner.ui.theme.LocalCustomColors

object TitleBarView {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TransparentTitleBackButtonTitleBar(
        modifier: Modifier,
        title: String,
        backButton: Int = R.drawable.ic_back_arrow,
        backgroundColor: Color,
        onClick: () -> Unit,
    ) {
        TopAppBar(
            modifier = modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(35.dp),
            title = {
                Column (
                    modifier = Modifier.height(32.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    ComposeTextView.TitleTextView(
                        text = title
                    )
                }
            },
            navigationIcon = {
                Column (
                    modifier = Modifier.height(32.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    ToolbarBackButton(backButton = backButton, onClick)
                }
            },
            colors = TopAppBarColors(
                containerColor = backgroundColor,
                scrolledContainerColor = backgroundColor,
                navigationIconContentColor = LocalCustomColors.current.titleTextColor,
                titleContentColor = LocalCustomColors.current.titleTextColor,
                actionIconContentColor = LocalCustomColors.current.titleTextColor
            )
        )
    }

    @Composable
    fun ToolbarBackButton(backButton: Int, onClick: () -> Unit) {
        IconButton(onClick = onClick) {
            ImageViewWitDrawable(
                imageId = backButton,
                modifier = Modifier
                    .width(26.dp)
                    .height(24.dp)
            )
        }
    }
}