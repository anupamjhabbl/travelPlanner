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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.screens.user.profile.entity.ProfileSocialScreens
import com.example.bbltripplanner.screens.vault.entity.VaultScreens
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun ProfileTpCommonSectionComposable(
    user: User,
    navController: NavController,
    isOwnProfile: Boolean,
    onFollowClick: () -> Unit
) {
    val storyCircleColor = if (user.userStory.isNullOrEmpty()) Color.Transparent else LocalCustomColors.current.secondaryBackground

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
                .size(100.dp)
                .clickable {
                    if (!user.userStory.isNullOrEmpty()) {
                        openTheUserStory(navController, user.userStory)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            ComposeImageView.CircularImageView(
                diameter = 90.dp,
                imageURI = user.profilePicture
            )
        }

        Spacer(Modifier.height(8.dp))

        ComposeTextView.TitleTextView(
            text = user.name,
        )

        ComposeTextView.TextView(
            text = user.bio,
            fontSize = 14.sp,
        )

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileStat(user.tripCount.toString(), stringResource(R.string.total_trips)) {
                openUserTripPage(user, navController)
            }
            ProfileStat(
                user.followersCount.toString(),
                stringResource(R.string.followers)
            ) {
                openFollowersPage(user, navController)
            }
            ProfileStat(user.followCount.toString(), stringResource(R.string.followings)) {
                openFollowingPage(user, navController)
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

private fun openFollowingPage(user: User, navController: NavController) {
    navController.navigate(
        AppNavigationScreen.ProfileSocialScreen.createRoute(ProfileSocialScreens.FOLLOWING.value, user.id)
    )
}

private fun openFollowersPage(user: User, navController: NavController) {
    navController.navigate(
        AppNavigationScreen.ProfileSocialScreen.createRoute(ProfileSocialScreens.FOLLOWERS.value, user.id)
    )
}

private fun openUserTripPage(user: User, navController: NavController) {
    navController.navigate(
        AppNavigationScreen.VaultScreen.createRoute(VaultScreens.TRIPS.value, user.id)
    )
}

private fun openTheUserStory(navController: NavController, userStory: String) {
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