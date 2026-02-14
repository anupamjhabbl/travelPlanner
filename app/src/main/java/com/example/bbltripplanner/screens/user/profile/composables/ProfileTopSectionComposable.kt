package com.example.bbltripplanner.screens.user.profile.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.user.profile.entity.ProfileSocialScreens
import com.example.bbltripplanner.screens.vault.entity.VaultScreens
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch

@Composable
fun ProfileTpCommonSectionComposable(
    user: User,
    isOwnProfile: Boolean,
    onFollowClick: () -> Unit
) {
    val storyCircleColor = if (user.userStory.isNullOrEmpty()) Color.Transparent else LocalCustomColors.current.secondaryBackground
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(60.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .border(3.dp, storyCircleColor, CircleShape)
                .size(120.dp)
                .clickable {
                    if (!user.userStory.isNullOrEmpty()) {
                        scope.launch {
                            openTheUserStory(user.userStory)
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            ComposeImageView.CircularImageView(
                diameter = 110.dp,
                imageURI = user.profilePicture ?: ""
            )
        }

        Spacer(Modifier.height(8.dp))

        ComposeTextView.TitleTextView(
            text = user.name,
        )

        ComposeTextView.TextView(
            text = user.bio ?: "",
            fontSize = 14.sp,
        )

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileStat(user.tripCount.toString(), stringResource(R.string.total_trips)) {
                scope.launch {
                    openUserTripPage(user)
                }
            }
            ProfileStat(
                user.followersCount.toString(),
                stringResource(R.string.followers)
            ) {
                scope.launch {
                    openFollowersPage(user)
                }
            }
            ProfileStat(user.followCount.toString(), stringResource(R.string.followings)) {
                scope.launch {
                    openFollowingPage(user)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        if  (!isOwnProfile) {
            Button(
                onClick = onFollowClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = LocalCustomColors.current.primaryBackground,
                    containerColor = LocalCustomColors.current.secondaryBackground
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.5f),
                contentPadding = PaddingValues(16.dp, 6.dp)
            ) {
                ComposeTextView.TitleTextView(
                    text = stringResource(R.string.follow),
                    textColor = LocalCustomColors.current.primaryBackground,
                    fontSize = 16.sp
                )
            }
        }
    }
}

private suspend fun openFollowingPage(user: User) {
    CommonNavigationChannel.navigateTo(
        NavigationAction.Navigate(
            AppNavigationScreen.ProfileSocialScreen.createRoute(
                ProfileSocialScreens.FOLLOWING.value,
                user.id
            )
        )
    )
}

private suspend fun openFollowersPage(user: User) {
    CommonNavigationChannel.navigateTo(
        NavigationAction.Navigate(
            AppNavigationScreen.ProfileSocialScreen.createRoute(
                ProfileSocialScreens.FOLLOWERS.value,
                user.id
            )
        )
    )
}

private suspend fun openUserTripPage(user: User) {
    CommonNavigationChannel.navigateTo(
        NavigationAction.Navigate(
            AppNavigationScreen.VaultScreen.createRoute(
                VaultScreens.TRIPS.value,
                user.id
            )
        )
    )
}

private suspend fun openTheUserStory(userStory: String) {
    // User Profile story page
}

@Composable
private fun ProfileStat(
    value: String,
    label: String,
    action: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                action()
            }
            .padding(8.dp)
    ) {
        ComposeTextView.TitleTextView(value, fontSize = 22.sp)
        ComposeTextView.TextView(label)
    }
}