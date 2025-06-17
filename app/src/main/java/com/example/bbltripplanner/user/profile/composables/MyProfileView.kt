package com.example.bbltripplanner.user.profile.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.user.profile.viewModels.OtherProfileIntent
import com.example.bbltripplanner.user.profile.viewModels.OtherProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun MyProfileView (
    navController: NavController
) {
    val viewModel: OtherProfileViewModel = koinViewModel()
    val viewEffect by viewModel.viewEffect.collectAsState(initial = null)
    var user by remember {
        mutableStateOf<User?>(null)
    }

    LaunchedEffect(key1 = true) {
        viewModel.processEvent(OtherProfileIntent.ViewEvent.FetchUserData)
    }

    LaunchedEffect(key1 = viewEffect) {
        when (viewEffect) {
            is OtherProfileIntent.ViewEffect.ShowUser -> {
                user = (viewEffect as OtherProfileIntent.ViewEffect.ShowUser).user
            }
            OtherProfileIntent.ViewEffect.UserFailure -> {}  // TODO
            null -> {}  // TODO
        }
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        topBar = {}
    ) {
        Column {
            ProfileUpperSection(navController)
            ProfileBottomSection()
        }
    }
}

@Composable
fun ProfileBottomSection() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.module_16))
    ) {
        val (leftBox, rightBox, line) = createRefs()
        Spacer(
            modifier = Modifier
                .padding(
                    dimensionResource(id = R.dimen.module_6half),
                    0.dp
                )
                .width(dimensionResource(id = R.dimen.module_7))
                .fillMaxHeight()
                .background(LocalCustomColors.current.primaryButtonBg)
                .constrainAs(line) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Column (
            modifier = Modifier
                .fillMaxHeight()
                .constrainAs(leftBox) {
                    start.linkTo(parent.start)
                    end.linkTo(line.end)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                }
        ) {
            TravelPointViewLeft(147.dp)
        }

        Column (
            modifier = Modifier
                .fillMaxHeight()
                .constrainAs(rightBox) {
                    start.linkTo(line.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                }
        ) {
            TravelPointViewRight(dimensionResource(id = R.dimen.module_4))
            TravelOnlyPointView(dimensionResource(id = R.dimen.module_8))
            TravelPointViewRight(123.dp)
        }
    }
}

@Composable
fun TravelOnlyPointView(
    topPadding: Dp
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, topPadding, 0.dp, 0.dp)
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.module_20))
                .height(dimensionResource(id = R.dimen.module_20))
                .background(LocalCustomColors.current.primaryBackground, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.module_14))
                    .height(dimensionResource(id = R.dimen.module_14))
                    .background(LocalCustomColors.current.primaryButtonBg, CircleShape)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun TravelPointViewRight(
    topPadding: Dp
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, topPadding, 0.dp, 0.dp)
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.module_20))
                .height(dimensionResource(id = R.dimen.module_20))
                .background(LocalCustomColors.current.primaryBackground, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.module_14))
                    .height(dimensionResource(id = R.dimen.module_14))
                    .background(LocalCustomColors.current.primaryButtonBg, CircleShape)
                    .align(Alignment.Center)
            )
        }
        TravelEntityClickable(dimensionResource(id = R.dimen.module_8), 0.dp)
    }
}

@Composable
fun TravelPointViewLeft(
    topPadding: Dp
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, topPadding, 0.dp, 0.dp)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.End
    ) {
        TravelEntityClickable(0.dp, dimensionResource(id = R.dimen.module_28))
    }
}

@Composable
fun CustomUIForToolbar() {
    com.example.bbltripplanner.common.composables.ComposeButtonView.IconButtonView(
        modifier = Modifier
            .padding(
                dimensionResource(id = R.dimen.module_8),
                dimensionResource(id = R.dimen.module_4),
                dimensionResource(id = R.dimen.module_16),
                dimensionResource(id = R.dimen.module_4)
            )
            .width(dimensionResource(id = R.dimen.module_10))
            .height(dimensionResource(id = R.dimen.module_25)),
        iconDrawable = R.drawable.ic_menu
    )
}

@Composable
fun ProfileUpperSection(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.module_420))
            .background(
                LocalCustomColors.current.primaryBackground,
                RoundedCornerShape(0, 0, 15, 15)
            )
    ) {
        Column {
            com.example.bbltripplanner.common.composables.ToolBarView.ToolbarWithCustomUI(
                customUI = {
                    CustomUIForToolbar()
                }
            ) {
                navController.popBackStack()
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.module_16))
            )
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                com.example.bbltripplanner.common.composables.ComposeImageView.CircularImageView(
                    imageURI = "https://www.orbitmedia.com/wp-content/uploads/2023/06/Andy-Profile-600.png",
                    diameter = dimensionResource(id = R.dimen.module_160)
                )
                com.example.bbltripplanner.common.composables.ComposeTextView.TitleTextView(
                    text = "Anupam Kumar",
                    fontSize = with(LocalDensity.current) {
                        dimensionResource(id = R.dimen.module_22sp).toSp()
                    },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.module_6), dimensionResource(id = R.dimen.module_10), 0.dp, 0.dp)
                )
                FollowViewGroup(
                    Modifier.padding(
                        dimensionResource(id = R.dimen.module_5),
                        dimensionResource(id = R.dimen.module_8),
                        0.dp,
                        dimensionResource(id = R.dimen.module_8)
                    )
                )
                com.example.bbltripplanner.common.composables.ComposeButtonView.PrimaryButtonView(
                    backgroundColor = LocalCustomColors.current.primaryButtonBg,
                    text = stringResource(id = R.string.follow),
                    textColor = LocalCustomColors.current.primaryButtonText
                ) {}
            }
        }
    }
}

@Composable
fun FollowViewGroup(
    modifier: Modifier
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FollowView("16", stringResource(id = R.string.followers))
        VerticalLine(
            width = dimensionResource(id = R.dimen.module_3),
            height = dimensionResource(id = R.dimen.module_28)
        )
        FollowView("18", stringResource(id = R.string.followings))
        VerticalLine(
            width = dimensionResource(id = R.dimen.module_3),
            height = dimensionResource(id = R.dimen.module_28)
        )
        FollowView("11", stringResource(id = R.string.expeditions))
    }
}

@Composable
fun FollowView(
    count: String,
    entity: String
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        com.example.bbltripplanner.common.composables.ComposeTextView.TextView(
            text = count
        )
        com.example.bbltripplanner.common.composables.ComposeTextView.TextView(
            text = entity
        )
    }
}

@Composable
fun VerticalLine(
    width: Dp,
    height: Dp
) {
    Spacer(
        modifier = Modifier
            .padding(
                dimensionResource(id = R.dimen.module_22),
                0.dp
            )
            .width(width)
            .height(height)
            .background(LocalCustomColors.current.titleTextColor)
    )
}

@Composable
fun TravelEntityClickable(
    leftPadding: Dp,
    rightPadding: Dp
) {
    Column (
        Modifier
            .wrapContentWidth()
            .height(dimensionResource(id = R.dimen.module_136))
            .padding(leftPadding, 0.dp, rightPadding, 0.dp)
            .background(
                LocalCustomColors.current.primaryBackground,
                RoundedCornerShape(dimensionResource(id = R.dimen.module_8))
            )
            .padding(dimensionResource(id = R.dimen.module_8))
    ) {
        com.example.bbltripplanner.common.composables.ComposeTextView.TitleTextView(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.module_4), 0.dp),
            text = "RaJagir Tour",
        )
        com.example.bbltripplanner.common.composables.ComposeTextView.TextView(
            text = "May 2023",
            fontSize = with(LocalDensity.current) {
                dimensionResource(id = R.dimen.module_18sp).toSp()
            },
            fontWeight = FontWeight.W400,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.module_4), 0.dp)
        )
        val list = listOf("Rjgir Mountains", "Pawapuri Temple", "Nalanada University")
        LazyColumn {
            items(list) {
                TravelPlaceListItem(text = it)
            }
        }
    }
}

@Composable
fun TravelPlaceListItem(
    text: String
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        com.example.bbltripplanner.common.composables.CommonDrawables.DotView(
            size = dimensionResource(id = R.dimen.module_5),
            color = LocalCustomColors.current.textColor,
            modifier = Modifier.padding(
                dimensionResource(id = R.dimen.module_4),
                0.dp,
                dimensionResource(id = R.dimen.module_6),
                0.dp
            )
        )
        com.example.bbltripplanner.common.composables.ComposeTextView.TextView(
            text = text,
            maxLines = 1,
            fontWeight = FontWeight.W400
        )
    }
}
