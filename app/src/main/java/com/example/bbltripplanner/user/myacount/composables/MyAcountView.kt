package com.example.bbltripplanner.user.myacount.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import com.example.bbltripplanner.user.myacount.entity.ProfileActionResourceMapper
import com.example.bbltripplanner.user.myacount.viewModels.MyAccountViewModel

@Composable
fun MyAccountView() {
    val viewModel = MyAccountViewModel()
    Scaffold (
        topBar = {
            com.example.bbltripplanner.common.composables.ToolBarView.TitleBackButtonTitleBar(
                modifier = Modifier,
                title = stringResource(id = R.string.profile),
                backgroundColor = Color.Transparent
            ) {}
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column (
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.module_16))
                    .wrapContentHeight()
            ) {
                ProfileContainer()
                LazyColumn (
                    modifier = Modifier
                        .padding(
                            0.dp,
                            dimensionResource(id = R.dimen.module_30),
                            0.dp, 
                            dimensionResource(id = R.dimen.module_16)
                        )
                ) {
                    items(ProfileActionResourceMapper.getProfileActions()) { item ->
                        ProfileActionTile(
                            vectorId = item.vectorId,
                            title = stringResource(id = item.title),
                            subTitle = stringResource(id = item.subTitle)
                        ) { key ->
                            takeAction(key)
                        }
                    }
                }
            }
        }

    }
}

fun takeAction(key: String) {}


@Composable
fun ProfileContainer() {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.module_8)))
            .background(LocalCustomColors.current.primaryAccent)
            .padding(dimensionResource(id = R.dimen.module_24))
    ) {
        Row (
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .align(Alignment.Center)
        ){
            com.example.bbltripplanner.common.composables.ComposeImageView.CircularImageView(
                imageURI = "https://delasign.com/delasignBlack.png",
                diameter = dimensionResource(id = R.dimen.module_90)
            )
            ProfileNameAndTravelsContainer(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.module_8), 0.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun ProfileNameAndTravelsContainer(
    modifier: Modifier
) {
    Column (
        modifier = modifier,
        Arrangement.Center
    ) {
        com.example.bbltripplanner.common.composables.ComposeTextView.TextView(
            text = "Anupam Kumar",
            fontSize = with(LocalDensity.current) {
                dimensionResource(id = R.dimen.module_20sp).toSp()
            },
            textColor = LocalCustomColors.current.textColor
        )
        com.example.bbltripplanner.common.composables.ComposeTextView.TextView(
            text = "8 Travels",
            fontSize = with(LocalDensity.current) {
                dimensionResource(id = R.dimen.module_18sp).toSp()
            },
            textColor = LocalCustomColors.current.textColor
        )
    }
}

@Composable
fun ProfileActionTile (
    vectorId: Int,
    title: String,
    subTitle: String,
    onClick: (key: String) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(
                    dimensionResource(id = R.dimen.module_8),
                    dimensionResource(id = R.dimen.module_20),
                    dimensionResource(id = R.dimen.module_8),
                    dimensionResource(id = R.dimen.module_8)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            com.example.bbltripplanner.common.composables.ComposeImageView.ImageViewWitDrawableId(
                imageId = vectorId,
                modifier = Modifier.height(dimensionResource(id = R.dimen.module_36)),
                contentDescription = title
            )

            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(
                        dimensionResource(id = R.dimen.module_16),
                        0.dp
                    )
            ) {
                com.example.bbltripplanner.common.composables.ComposeTextView.TextView(
                    text = title,
                    fontSize = with(LocalDensity.current) {
                        dimensionResource(id = R.dimen.module_18sp).toSp()
                    },
                    fontWeight = FontWeight.W400
                )
                com.example.bbltripplanner.common.composables.ComposeTextView.TextView(
                    text = subTitle,
                    fontSize = with(LocalDensity.current) {
                        dimensionResource(id = R.dimen.module_16sp).toSp()
                    },
                    fontWeight = FontWeight.W400
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            com.example.bbltripplanner.common.composables.ComposeImageView.ImageViewWitDrawableId(
                imageId = R.drawable.ic_right_arrow,
                modifier = Modifier.height(dimensionResource(id = R.dimen.module_20)),
                contentDescription = "right arrow"
            )
        }
    }
}
