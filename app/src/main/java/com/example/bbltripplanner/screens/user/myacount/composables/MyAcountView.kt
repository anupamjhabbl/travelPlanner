package com.example.bbltripplanner.screens.user.myacount.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionItem
import com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionResourceMapper
import com.example.bbltripplanner.screens.user.myacount.viewModels.MyAccountViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyAccountView(
    navController: NavController
) {
    val viewModel: MyAccountViewModel = koinViewModel()
    val user = viewModel.getUser()

    Column(
        modifier = Modifier
            .background(LocalCustomColors.current.primaryBackground)
            .fillMaxWidth()
    ) {
        AccountToolbar(navController)

        if (user == null) {
            ComposeViewUtils.FullScreenErrorComposable(
                Pair(
                    stringResource(R.string.inconvenience_sorry),
                    stringResource(R.string.restart_app_message)
                ),
                false
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                ) {
                    ProfileContainer(user) {
                        navController.navigate(AppNavigationScreen.VaultScreen.route)
                    }

                    LazyColumn(
                        modifier = Modifier
                            .padding(
                                0.dp,
                                dimensionResource(id = R.dimen.module_16),
                            )
                    ) {
                        items(ProfileActionResourceMapper.getProfileActions()) { item ->
                            ProfileActionTile(
                                item
                            ) { key ->
                                takeAction(navController, key)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountToolbar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                modifier = Modifier.size(32.dp),
                contentDescription = "Back",
                tint = LocalCustomColors.current.secondaryBackground
            )
        }

        Spacer(Modifier.width(8.dp))

        ComposeTextView.TitleTextView(
            text = stringResource(R.string.profile),
            fontSize = 22.sp,
            textColor = LocalCustomColors.current.secondaryBackground
        )
    }

}

private fun takeAction(navController: NavController, key: String) {
    when (key) {
        Constants.PROFILE_DETAILS -> openMyProfilePage(navController)
        Constants.NOTIFICATIONS -> {}
        Constants.SETTINGS -> {}
        Constants.HELP_SUPPORT -> {}
        Constants.LOGOUT -> {}
    }
}

fun openMyProfilePage(navController: NavController) {
    navController.navigate(AppNavigationScreen.ProfileScreen.route)
}


@Composable
fun ProfileContainer(
    user: User,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.module_16)))
            .background(LocalCustomColors.current.defaultImageCardColor)
            .clickable {
                onClick()
            }
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
                imageURI = user.profilePicture,
                diameter = dimensionResource(id = R.dimen.module_90)
            )
            ProfileNameAndTravelsContainer(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.module_16), 0.dp)
                    .fillMaxHeight(),
                user
            )
        }
    }
}

@Composable
fun ProfileNameAndTravelsContainer(
    modifier: Modifier,
    user: User
) {
    Column (
        modifier = modifier,
        Arrangement.Center
    ) {
        ComposeTextView.TextView(
            text = user.name,
            fontSize = with(LocalDensity.current) {
                dimensionResource(id = R.dimen.module_20sp).toSp()
            },
            textColor = LocalCustomColors.current.textColor
        )

        Spacer(Modifier.height(4.dp))

        ComposeTextView.TextView(
            text = stringResource(R.string.trip_count, user.tripCount),
            fontSize = with(LocalDensity.current) {
                dimensionResource(id = R.dimen.module_18sp).toSp()
            },
            textColor = LocalCustomColors.current.textColor
        )
    }
}

@Composable
fun ProfileActionTile (
    item: ProfileActionItem,
    onClick: (key: String) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(8.dp, 0.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(item.key) }
            .padding(8.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(
                    dimensionResource(id = R.dimen.module_8),
                    dimensionResource(id = R.dimen.module_12)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            com.example.bbltripplanner.common.composables.ComposeImageView.ImageViewWitDrawableId(
                imageId = item.vectorId,
                modifier = Modifier.height(dimensionResource(id = R.dimen.module_36)),
                contentDescription = stringResource(id = item.title)
            )

            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(32.dp, 0.dp, 0.dp, 0.dp)
            ) {
                ComposeTextView.TextView(
                    text = stringResource(id = item.title),
                    fontSize = with(LocalDensity.current) {
                        dimensionResource(id = R.dimen.module_18sp).toSp()
                    },
                    fontWeight = FontWeight.W500
                )
                ComposeTextView.TextView(
                    text = stringResource(id = item.title),
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
