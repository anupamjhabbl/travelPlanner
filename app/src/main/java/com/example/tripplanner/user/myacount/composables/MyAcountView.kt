package com.example.tripplanner.user.myacount.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripplanner.R
import com.example.tripplanner.common.composables.ComposeImageView
import com.example.tripplanner.common.composables.ComposeTextView
import com.example.tripplanner.common.composables.TitleBarView
import com.example.tripplanner.ui.theme.LocalCustomColors

@Composable
fun MyAccountView() {
    Scaffold (
        topBar = {
            TitleBarView.TransparentTitleBackButtonTitleBar(
                modifier = Modifier,
                title = stringResource(id = R.string.profile),
                onClick = {
                    // do nothing
                }
            )
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
            }
        }

    }
}


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
            ComposeImageView.CircularImageView(
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
        ComposeTextView.TextView(
            text = "Anupam Kumar",
            fontSize = dimensionResource(id = R.dimen.module_18sp).value.sp,
            textColor = LocalCustomColors.current.textColor
        )
        ComposeTextView.TextView(
            text = "8 Travels",
            fontSize = dimensionResource(id = R.dimen.module_14sp).value.sp,
            textColor = LocalCustomColors.current.textColor
        )
    }
}