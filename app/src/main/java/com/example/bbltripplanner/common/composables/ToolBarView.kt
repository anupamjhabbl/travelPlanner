package com.example.bbltripplanner.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView.ImageViewWitDrawableId
import com.example.bbltripplanner.ui.theme.LocalCustomColors

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
                .padding(dimensionResource(id = R.dimen.module_8), dimensionResource(id = R.dimen.module_16))
                .height(dimensionResource(id = R.dimen.module_36)),
            title = {
                Column (
                    modifier = Modifier.height(dimensionResource(id = R.dimen.module_32)),
                    verticalArrangement = Arrangement.Center
                ) {
                    ComposeTextView.TitleTextView(
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.module_4), 0.dp),
                        text = title
                    )
                }
            },
            navigationIcon = {
                Column (
                    modifier = Modifier.height(dimensionResource(id = R.dimen.module_32)),
                    verticalArrangement = Arrangement.Center
                ) {
                    ToolbarBackButton(
                        backButton = backButton,
                        onClick
                    )
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
            ImageViewWitDrawableId(
                imageId = backButton,
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.module_32))
                    .height(dimensionResource(id = R.dimen.module_28))
            )
        }
    }

    @Composable
    fun SimpleToolbarWithBackButton(
        title: String,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(32.dp)
                ) {
                    IconButton(
                        onClick = onClick
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = LocalCustomColors.current.secondaryBackground,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                ComposeTextView.TextView(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    textColor = LocalCustomColors.current.secondaryBackground
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ToolbarWithCustomUI (
        title: String = com.example.bbltripplanner.common.Constants.EMPTY_STRING,
        backButton: Int = R.drawable.ic_back_arrow,
        backgroundColor: Color = Color.Transparent,
        customUI: @Composable RowScope.() -> Unit,
        onBackButtonClick: () -> Unit
    ) {
        TopAppBar(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.module_8), dimensionResource(id = R.dimen.module_16))
                .height(dimensionResource(id = R.dimen.module_36)),
            title = {
                Column (
                    modifier = Modifier.height(dimensionResource(id = R.dimen.module_32)),
                    verticalArrangement = Arrangement.Center
                ) {
                    ComposeTextView.TitleTextView(
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.module_6), 0.dp),
                        text = title
                    )
                }
            },
            navigationIcon = {
                Column (
                    modifier = Modifier.height(dimensionResource(id = R.dimen.module_32)),
                    verticalArrangement = Arrangement.Center
                ) {
                    ToolbarBackButton(
                        backButton = backButton,
                        onBackButtonClick
                    )
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