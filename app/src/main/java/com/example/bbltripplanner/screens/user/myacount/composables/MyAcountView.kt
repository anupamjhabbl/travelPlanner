package com.example.bbltripplanner.screens.user.myacount.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionItem
import com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionResourceMapper
import com.example.bbltripplanner.screens.user.myacount.viewModels.MyAccountViewModel
import com.example.bbltripplanner.screens.vault.entity.VaultScreens
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyAccountView(
    navController: NavController
) {
    val viewModel: MyAccountViewModel = koinViewModel()
    var logOutConfirmDialogVisibility by remember {
        mutableStateOf(false)
    }
    val user = viewModel.getUser()

    if (logOutConfirmDialogVisibility) {
        AlertDialog(
            onDismissRequest = { logOutConfirmDialogVisibility = false },
            confirmButton = {
                ConfirmButton {
                    logOutConfirmDialogVisibility = false
                    viewModel.logOutUser()
                    navController.navigate(AppNavigationScreen.AuthGraph.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop
                    }
                }
            },
            dismissButton = {
                DismissButton {
                    logOutConfirmDialogVisibility = false
                }
            },
            text = {
                ComposeTextView.TextView(
                    stringResource(R.string.logout_alert_message),
                    fontSize = 14.sp
                )
            },
            title = {
                ComposeTextView.TitleTextView(
                    stringResource(R.string.logout_alert_title)
                )
            },
            shape = RoundedCornerShape(12.dp),
            containerColor = LocalCustomColors.current.defaultImageCardColor
        )
    }

    fun takeAction(navController: NavController, key: String) {
        when (key) {
            Constants.PROFILE_DETAILS -> openMyProfilePage(navController, user?.id)
            Constants.NOTIFICATIONS -> navController.navigate(AppNavigationScreen.NotificationScreen.route)
            Constants.SETTINGS -> navController.navigate(AppNavigationScreen.UserSettingsScreen.route)
            Constants.HELP_SUPPORT -> navController.navigate(AppNavigationScreen.HelpSupportScreen.route)
            Constants.LOGOUT ->  { logOutConfirmDialogVisibility = true }
        }
    }

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
                )
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
                    ProfileContainer(user, navController) {
                        navController.navigate(AppNavigationScreen.VaultScreen.createRoute(VaultScreens.TRIPS.value, user.id))
                    }

                    Spacer(Modifier.height(20.dp))

                    LazyColumn(
                        modifier = Modifier
                            .padding(
                                0.dp,
                                dimensionResource(id = R.dimen.module_16),
                            )
                    ) {
                        items(ProfileActionResourceMapper.getAccountActions()) { item ->
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
private fun AccountToolbar(navController: NavController) {
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

@Composable
fun ConfirmButton(
    onClick: () -> Unit
) {
    Box(modifier = Modifier.padding(4.dp)) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(16.dp, 8.dp),
            border = BorderStroke(2.dp, LocalCustomColors.current.secondaryBackground)
        ) {
            ComposeTextView.TextView(
                text = stringResource(R.string.logout),
                textColor = LocalCustomColors.current.secondaryBackground,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun DismissButton(
    onClick: () -> Unit
) {
    Box(modifier = Modifier.padding(4.dp)) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = LocalCustomColors.current.secondaryBackground,
                contentColor = LocalCustomColors.current.primaryBackground
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(16.dp, 8.dp)
        ) {
            ComposeTextView.TextView(
                text = stringResource(R.string.cancel),
                textColor = LocalCustomColors.current.primaryBackground,
                fontSize = 16.sp
            )
        }
    }
}

private fun openMyProfilePage(navController: NavController, userId: String?) {
    userId?.let {
        navController.navigate(
            AppNavigationScreen.ProfileScreen.createRoute(userId)
        )
    }
}


@Composable
private fun ProfileContainer(
    user: User,
    navController: NavController,
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
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
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

            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = {
                    navController.navigate(AppNavigationScreen.EditProfileScreen.route)
                }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Back",
                    tint = LocalCustomColors.current.secondaryBackground
                )
            }
        }
    }
}

@Composable
private fun ProfileNameAndTravelsContainer(
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
private fun ProfileActionTile (
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
