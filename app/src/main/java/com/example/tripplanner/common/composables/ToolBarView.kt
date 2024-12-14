package com.example.tripplanner.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.res.dimensionResource
import com.example.tripplanner.R
import com.example.tripplanner.common.Constants
import com.example.tripplanner.common.composables.ComposeImageView.ImageViewWitDrawable
import com.example.tripplanner.ui.theme.LocalCustomColors

object ToolBarView {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TitleBackButtonTitleBar(
        modifier: Modifier = Modifier,
        title: String,
        backButton: Int = R.drawable.ic_back_arrow,
        backgroundColor: Color = Color.Transparent,
        onClick: () -> Unit,
    ) {
        TopAppBar(
            modifier = modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.module_36)),
            title = {
                Column (
                    modifier = Modifier.height(dimensionResource(id = R.dimen.module_32)),
                    verticalArrangement = Arrangement.Center
                ) {
                    ComposeTextView.TitleTextView(
                        text = title
                    )
                }
            },
            navigationIcon = {
                Column (
                    modifier = Modifier.height(dimensionResource(id = R.dimen.module_32)),
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
    private fun ToolbarBackButton(backButton: Int, onClick: () -> Unit) {
        IconButton(onClick = onClick) {
            ImageViewWitDrawable(
                imageId = backButton,
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.module_26))
                    .height(dimensionResource(id = R.dimen.module_24))
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ToolbarWithCustomUI (
        title: String = Constants.EMPTY_STRING,
        backButton: Int = R.drawable.ic_back_arrow,
        backgroundColor: Color = Color.Transparent,
        customUI: @Composable RowScope.() -> Unit,
        onBackButtonClick: () -> Unit
    ) {
        TopAppBar(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.module_36)),
            title = {
                Column (
                    modifier = Modifier.height(dimensionResource(id = R.dimen.module_32)),
                    verticalArrangement = Arrangement.Center
                ) {
                    ComposeTextView.TitleTextView(
                        text = title
                    )
                }
            },
            navigationIcon = {
                Column (
                    modifier = Modifier.height(dimensionResource(id = R.dimen.module_32)),
                    verticalArrangement = Arrangement.Center
                ) {
                    ToolbarBackButton(backButton = backButton, onBackButtonClick)
                }
            },
            colors = TopAppBarColors(
                containerColor = backgroundColor,
                scrolledContainerColor = backgroundColor,
                navigationIconContentColor = LocalCustomColors.current.titleTextColor,
                titleContentColor = LocalCustomColors.current.titleTextColor,
                actionIconContentColor = LocalCustomColors.current.titleTextColor
            ),
            actions = customUI
        )
    }
}