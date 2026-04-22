package com.example.bbltripplanner.screens.user.general.composables

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.CommonLifecycleAwareLaunchedEffect
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.user.general.viewModels.LogOutState
import com.example.bbltripplanner.screens.user.general.viewModels.UserSettingsIntent
import com.example.bbltripplanner.screens.user.general.viewModels.UserSettingsViewModel
import com.example.bbltripplanner.screens.user.myacount.composables.ConfirmButton
import com.example.bbltripplanner.screens.user.myacount.composables.DismissButton
import com.example.bbltripplanner.screens.user.profile.entity.ProfileSocialScreens
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserSettingsScreen() {
    val customColors = LocalCustomColors.current
    val context = LocalContext.current
    val viewModel: UserSettingsViewModel = koinViewModel()
    val user = viewModel.getUser()
    val scope = rememberCoroutineScope()
    val message = stringResource(R.string.generic_error)
    var isLoading by remember {
        mutableStateOf(false)
    }
    var logOutConfirmDialogVisibility by remember {
        mutableStateOf(false)
    }

    CommonLifecycleAwareLaunchedEffect(
        viewModel.logOutResultState
    ) { viewState ->
        when (viewState) {
            LogOutState.LOADING -> {
                isLoading = true
            }
            LogOutState.SUCCESS -> {
                isLoading = false
                logoutSuccess()
            }
            LogOutState.FAILURE -> {
                isLoading = false
                logoutFailure(context, message)
            }
        }
    }

    if (logOutConfirmDialogVisibility) {
        AlertDialog(
            onDismissRequest = { logOutConfirmDialogVisibility = false },
            confirmButton = {
                ConfirmButton {
                    logOutConfirmDialogVisibility = false
                    viewModel.processEvent(UserSettingsIntent.ViewEvent.LogoutUser)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
            .verticalScroll(rememberScrollState())
    ) {
        UserSettingsToolbar()

        if (isLoading) {
            ComposeViewUtils.FullScreenLoading()
            return@Column
        }

        Spacer(modifier = Modifier.height(8.dp))

        SettingsSectionHeader(title = stringResource(R.string.account))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(customColors.fadedBackground.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        scope.launch {
                            user?.id?.let {
                            CommonNavigationChannel.navigateTo(NavigationAction.Navigate(AppNavigationScreen.ProfileScreen.createRoute(it)))
                                }
                        }
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ComposeImageView.CircularImageView(
                    imageURI = user?.profilePicture ?: "",
                    diameter = 56.dp
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    ComposeTextView.TitleTextView(
                        text = user?.name ?: "User1",
                        fontSize = 18.sp
                    )
                    ComposeTextView.TextView(
                        text = user?.bio ?: "User1",
                        textColor = customColors.hintTextColor,
                        fontSize = 14.sp
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = customColors.hintTextColor
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = customColors.defaultImageCardColor.copy(alpha = 0.5f))

            SettingsItem(
                icon = Icons.Default.Person,
                title = stringResource(R.string.edit_profile),
                subtitle = stringResource(R.string.update_name_bio_photo),
                onClick = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(NavigationAction.Navigate(AppNavigationScreen.EditProfileScreen.route))
                    }
                }
            )
            SettingsItem(
                icon = Icons.Default.Lock,
                title = stringResource(R.string.password_security),
                subtitle = stringResource(R.string.change_password_description),
                onClick = { /* Handle Click */ }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        SettingsSectionHeader(title = stringResource(R.string.privacy_social_header))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(customColors.fadedBackground.copy(alpha = 0.2f))
        ) {
            SettingsItem(
                icon = Icons.Default.PrivacyTip,
                title = stringResource(R.string.privacy_visbility_title),
                subtitle = stringResource(R.string.privacy_visibility_description),
                onClick = { /* Handle Click */ }
            )
            SettingsItem(
                icon = Icons.Default.Group,
                title = stringResource(R.string.manage_followers_title),
                subtitle = stringResource(R.string.manage_followers_description),
                onClick = {
                    user?.id?.let {
                        scope.launch {
                            CommonNavigationChannel.navigateTo(
                                NavigationAction.Navigate(
                                    AppNavigationScreen.ProfileSocialScreen.createRoute(
                                        ProfileSocialScreens.FOLLOWERS.value,
                                        it
                                    )
                                )
                            )
                        }
                    }
                }
            )
            SettingsItem(
                icon = Icons.Default.Block,
                title = stringResource(R.string.blocked_users),
                subtitle = stringResource(R.string.block_description),
                onClick = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(
                                AppNavigationScreen.BlockedUsersScreen.route
                            )
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        SettingsSectionHeader(title = stringResource(R.string.trip_app_preferences_header))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(customColors.fadedBackground.copy(alpha = 0.2f))
        ) {
            SettingsItem(
                icon = Icons.Default.Palette,
                title = stringResource(R.string.appearance_title),
                subtitle = stringResource(R.string.appearance_subtitle),
                trailing = {
                    Box(
                        modifier = Modifier
                            .background(customColors.success.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        ComposeTextView.TextView(
                            text = stringResource(R.string.system_default),
                            textColor = customColors.success,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                onClick = { /* Handle Click */ }
            )
            SettingsItem(
                icon = Icons.Default.Language,
                title = stringResource(R.string.language_region_title),
                subtitle = stringResource(R.string.language_region_subtitle),
                onClick = { /* Handle Click */ }
            )
            SettingsItem(
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                title = stringResource(R.string.help_support),
                subtitle = stringResource(R.string.help_support_description),
                onClick = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(NavigationAction.Navigate(AppNavigationScreen.HelpSupportScreen.route))
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        SettingsSectionHeader(title = stringResource(R.string.legal_about_header))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(customColors.fadedBackground.copy(alpha = 0.2f))
        ) {
            SettingsItem(
                icon = Icons.Default.Description,
                title = stringResource(R.string.terms_of_use_title),
                subtitle = stringResource(R.string.terms_of_use_subtitle),
                onClick = { /* Handle Click */ }
            )
            SettingsItem(
                icon = Icons.Default.Security,
                title = stringResource(R.string.privacy_policy_title),
                subtitle = stringResource(R.string.privacy_policy_subtitle),
                onClick = { /* Handle Click */ }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ComposeTextView.TextView(
            text = stringResource(R.string.danger_zone_header),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            textColor = customColors.error,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(customColors.error.copy(alpha = 0.06f))
        ) {
            SettingsItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = stringResource(R.string.logout),
                subtitle = stringResource(R.string.logout_description),
                titleColor = customColors.error,
                iconColor = customColors.error,
                onClick = {
                    logOutConfirmDialogVisibility = true
                }
            )
            SettingsItem(
                icon = Icons.Default.Delete,
                title = stringResource(R.string.delete_account_title),
                subtitle = stringResource(R.string.delete_account_subtitle),
                titleColor = customColors.error,
                iconColor = customColors.error,
                onClick = { /* Handle Click */ }
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

private fun logoutFailure(context: Context, message: String) {
    ComposeViewUtils.showToast(context, message)
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

@Composable
fun SettingsSectionHeader(title: String) {
    ComposeTextView.TextView(
        text = title,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        textColor = LocalCustomColors.current.secondaryBackground,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    titleColor: Color = LocalCustomColors.current.titleTextColor,
    iconColor: Color = LocalCustomColors.current.secondaryBackground,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            ComposeTextView.TitleTextView(
                text = title,
                fontSize = 16.sp,
                textColor = titleColor
            )
            ComposeTextView.TextView(
                text = subtitle,
                textColor = customColors.hintTextColor,
                fontSize = 13.sp
            )
        }
        
        if (trailing != null) {
            trailing()
            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = customColors.hintTextColor
        )
    }
}


@Composable
private fun UserSettingsToolbar() {
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                }
            }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                modifier = Modifier.size(32.dp),
                contentDescription = "Back",
                tint = LocalCustomColors.current.secondaryBackground
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.padding(top = 7.dp)
        ) {
            ComposeTextView.TitleTextView(
                text = stringResource(R.string.settings),
                fontSize = 24.sp
            )
            ComposeTextView.TextView(
                text = stringResource(R.string.settings_description),
                textColor = customColors.hintTextColor,
                fontSize = 14.sp
            )
        }
    }
}
