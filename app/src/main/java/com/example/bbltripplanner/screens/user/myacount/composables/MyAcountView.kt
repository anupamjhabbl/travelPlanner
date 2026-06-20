package com.example.bbltripplanner.screens.user.myacount.composables

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionItem
import com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionResourceMapper
import com.example.bbltripplanner.screens.user.myacount.viewModels.MyAccountIntent
import com.example.bbltripplanner.screens.user.myacount.viewModels.MyAccountViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyAccountView() {
    val viewModel: MyAccountViewModel = koinViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val message = stringResource(R.string.generic_error)
    var logOutConfirmDialogVisibility by remember {
        mutableStateOf(false)
    }
    var isLoading by remember { mutableStateOf(false) }
    val user = viewModel.getUser()

    LaunchedEffect(Unit) {
        viewModel.viewState.collectLatest { viewState ->
            when (viewState) {
                MyAccountIntent.ViewState.LogoutFailure -> {
                    isLoading = false
                    logoutFailure(context, message)
                }
                MyAccountIntent.ViewState.LogoutSuccess -> {
                    isLoading = false
                    logoutSuccess()
                }
                MyAccountIntent.ViewState.Loading -> {
                    isLoading = true
                }
            }
        }
    }

    if (logOutConfirmDialogVisibility) {
        ComposeViewUtils.ConfirmationDialog(
            title = stringResource(R.string.logout_alert_title),
            message = stringResource(R.string.logout_alert_message),
            confirmButtonText = stringResource(R.string.logout),
            isConfirmPositive = false,
            onConfirm = {
                logOutConfirmDialogVisibility = false
                viewModel.processEvent(MyAccountIntent.ViewEvent.LogoutUser)
            },
            onDismiss = { logOutConfirmDialogVisibility = false }
        )
    }

    suspend fun takeAction(key: String) {
        when (key) {
            Constants.PROFILE_DETAILS -> openMyProfilePage(user?.id)
            Constants.NOTIFICATIONS -> CommonNavigationChannel.navigateTo(NavigationAction.Navigate(AppNavigationScreen.NotificationScreen.route))
            Constants.SETTINGS -> CommonNavigationChannel.navigateTo(NavigationAction.Navigate(AppNavigationScreen.UserSettingsScreen.route))
            Constants.HELP_SUPPORT -> CommonNavigationChannel.navigateTo(NavigationAction.Navigate(AppNavigationScreen.HelpSupportScreen.route))
            Constants.LOGOUT ->  { logOutConfirmDialogVisibility = true }
        }
    }

    if (isLoading) {
        ComposeViewUtils.FullScreenLoading()
    } else {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .background(LocalCustomColors.current.primaryBackground)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            AccountToolbar()

            if (user == null) {
                ComposeViewUtils.FullScreenErrorComposable(
                    Pair(
                        stringResource(R.string.inconvenience_sorry),
                        stringResource(R.string.restart_app_message)
                    )
                )
            } else {
                val accountActions = ProfileActionResourceMapper.getAccountActions()
                val settingsActions = accountActions.filter { it.key != Constants.LOGOUT }
                val logoutAction = accountActions.firstOrNull { it.key == Constants.LOGOUT }

                Spacer(Modifier.height(8.dp))

                ProfileContainer(user) {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(
                                destination = AppNavigationScreen.VaultScreen.route
                            )
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                SettingsBlockCard(
                    actions = settingsActions,
                    onActionClick = { key ->
                        scope.launch {
                            takeAction(key)
                        }
                    }
                )

                Spacer(Modifier.height(20.dp))

                LogoutButtonCard(
                    logoutAction = logoutAction,
                    onActionClick = { key ->
                        scope.launch {
                            takeAction(key)
                        }
                    }
                )

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun AccountToolbar() {
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(customColors.secondaryBackground.copy(alpha = 0.12f))
                .clickable {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = customColors.secondaryBackground,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        ComposeTextView.TitleTextView(
            text = stringResource(R.string.profile),
            fontSize = 20.sp,
            textColor = customColors.titleTextColor
        )
    }
}

private suspend fun openMyProfilePage(userId: String?) {
    userId?.let {
        CommonNavigationChannel.navigateTo(
            NavigationAction.Navigate(
                AppNavigationScreen.ProfileScreen.createRoute(userId)
            )
        )
    }
}

private suspend fun logoutSuccess() {
    CommonNavigationChannel.navigateTo(
        NavigationAction.Navigate(
            AppNavigationScreen.AuthGraph.route
        ) {
            popUpTo(0) { inclusive = true }
            launchSingleTop
        }
    )
}

private fun logoutFailure(context: Context, message: String) {
    ComposeViewUtils.showToast(context, message)
}

@Composable
private fun ProfileContainer(
    user: User,
    onClick: () -> Unit
) {
    val customColors = LocalCustomColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(customColors.fadedBackground.copy(alpha = 0.35f))
            .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .border(2.5.dp, customColors.secondaryBackground.copy(alpha = 0.6f), CircleShape)
                        .padding(3.dp)
                ) {
                    com.example.bbltripplanner.common.composables.ComposeImageView.CircularImageView(
                        imageURI = user.profilePicture ?: "",
                        diameter = 70.dp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    ComposeTextView.TextView(
                        text = user.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textColor = customColors.titleTextColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ComposeTextView.TextView(
                        text = user.bio ?: "",
                        fontSize = 13.sp,
                        textColor = customColors.hintTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(customColors.defaultImageCardColor.copy(alpha = 0.4f))
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(label = "Trips", count = user.tripCount)
                StatDivider()
                StatItem(label = "Followers", count = user.followersCount)
                StatDivider()
                StatItem(label = "Following", count = user.followCount)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(customColors.secondaryBackground.copy(alpha = 0.1f))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ComposeTextView.TextView(
                    text = "Explore Travel Vault",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textColor = customColors.secondaryBackground
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_right_arrow),
                    contentDescription = null,
                    tint = customColors.secondaryBackground,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun StatItem(label: String, count: Long) {
    val customColors = LocalCustomColors.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ComposeTextView.TextView(
            text = count.toString(),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            textColor = customColors.titleTextColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        ComposeTextView.TextView(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textColor = customColors.hintTextColor
        )
    }
}

@Composable
private fun StatDivider() {
    val customColors = LocalCustomColors.current
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(24.dp)
            .background(customColors.defaultImageCardColor.copy(alpha = 0.8f))
    )
}

@Composable
private fun SettingsBlockCard(
    actions: List<ProfileActionItem>,
    onActionClick: (String) -> Unit
) {
    val customColors = LocalCustomColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(customColors.defaultImageCardColor.copy(alpha = 0.35f))
            .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(24.dp))
    ) {
        actions.forEachIndexed { index, item ->
            ProfileActionTile(
                item = item,
                onClick = onActionClick
            )
            if (index < actions.size - 1) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(1.dp)
                        .background(customColors.defaultImageCardColor.copy(alpha = 0.8f))
                )
            }
        }
    }
}

@Composable
private fun ProfileActionTile(
    item: ProfileActionItem,
    onClick: (key: String) -> Unit
) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item.key) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(customColors.secondaryBackground.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            com.example.bbltripplanner.common.composables.ComposeImageView.ImageViewWitDrawableId(
                imageId = item.vectorId,
                modifier = Modifier.size(22.dp),
                contentDescription = stringResource(id = item.title)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            ComposeTextView.TextView(
                text = stringResource(id = item.title),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textColor = customColors.titleTextColor
            )

            Spacer(Modifier.height(2.dp))

            ComposeTextView.TextView(
                text = stringResource(id = item.subTitle),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textColor = customColors.hintTextColor
            )
        }

        Icon(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_right_arrow),
            contentDescription = "Navigate",
            tint = customColors.secondaryBackground.copy(alpha = 0.7f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun LogoutButtonCard(
    logoutAction: ProfileActionItem?,
    onActionClick: (String) -> Unit
) {
    if (logoutAction == null) return
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(customColors.defaultImageCardColor.copy(alpha = 0.35f))
            .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(20.dp))
            .clickable { onActionClick(logoutAction.key) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(customColors.secondaryBackground.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            com.example.bbltripplanner.common.composables.ComposeImageView.ImageViewWitDrawableId(
                imageId = logoutAction.vectorId,
                modifier = Modifier.size(22.dp),
                contentDescription = stringResource(id = logoutAction.title)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            ComposeTextView.TextView(
                text = stringResource(id = logoutAction.title),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textColor = customColors.titleTextColor
            )

            Spacer(Modifier.height(2.dp))

            ComposeTextView.TextView(
                text = stringResource(id = logoutAction.subTitle),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textColor = customColors.hintTextColor
            )
        }

        Icon(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_right_arrow),
            contentDescription = "Logout",
            tint = customColors.secondaryBackground.copy(alpha = 0.7f),
            modifier = Modifier.size(16.dp)
        )
    }
}
