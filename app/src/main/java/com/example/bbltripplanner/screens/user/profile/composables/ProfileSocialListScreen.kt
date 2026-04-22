package com.example.bbltripplanner.screens.user.profile.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileFollowersViewModel
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileFollowingViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProfileFollowersPage(
    userId: String?
) {
    val scope = rememberCoroutineScope()
    val emptyTitle = stringResource(R.string.followers_empty_heading)
    val emptyContent = stringResource(R.string.followers_empty_content)

    val viewModel: ProfileFollowersViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val uiState by viewModel.userList.collectAsStateWithLifecycle()
    val isSelf = viewModel.isSelfProfile()

    if (uiState.isLoading) {
        ComposeViewUtils.FullScreenLoading()
    } else if (uiState.data == null || uiState.error != null) {
        ComposeViewUtils.FullScreenErrorComposable(
            errorStrings = Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
        )
    } else {
        if (uiState.data.isNullOrEmpty()) {
            ComposeViewUtils.FullScreenErrorComposable(
                errorStrings = Pair(emptyTitle, emptyContent)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = uiState.data as List<User>,
                    key = { it.id }
                ) { user ->
                    SocialProfileItem(
                        user = user,
                        actionLabel = if (isSelf) stringResource(R.string.follow) else null,
                        onActionClick = {
                            viewModel.followUser(user.id)
                        },
                        onUserClick = {
                            scope.launch {
                                CommonNavigationChannel.navigateTo(
                                    NavigationAction.Navigate(
                                        AppNavigationScreen.ProfileScreen.createRoute(
                                            user.id
                                        )
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileFollowingPage(
    userId: String?
) {
    val scope = rememberCoroutineScope()
    val emptyTitle = stringResource(R.string.following_empty_heading)
    val emptyContent = stringResource(R.string.following_empty_content)
    val viewModel: ProfileFollowingViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val uiState by viewModel.userList.collectAsStateWithLifecycle()
    val isSelf = viewModel.isSelfProfile()

    if (uiState.isLoading) {
        ComposeViewUtils.FullScreenLoading()
    } else if (uiState.data == null || uiState.error != null) {
        ComposeViewUtils.FullScreenErrorComposable(
            errorStrings = Pair(Constants.DEFAULT_ERROR, uiState.error ?: "")
        )
    } else {
        if (uiState.data.isNullOrEmpty()) {
            ComposeViewUtils.FullScreenErrorComposable(
                errorStrings = Pair(emptyTitle, emptyContent)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = uiState.data as List<User>,
                    key = { it.id }
                ) { user ->
                    SocialProfileItem(
                        user = user,
                        actionLabel = if (isSelf) stringResource(R.string.unfollow) else null,
                        onActionClick = {
                            viewModel.unfollowUser(user.id)
                        },
                        onUserClick = {
                            scope.launch {
                                CommonNavigationChannel.navigateTo(
                                    NavigationAction.Navigate(
                                        AppNavigationScreen.ProfileScreen.createRoute(
                                            user.id
                                        )
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SocialProfileItem(
    user: User,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {},
    onUserClick: (String) -> Unit
) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(customColors.defaultImageCardColor)
            .clickable { onUserClick(user.id) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = user.profilePicture ?: "",
            contentDescription = null,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(customColors.secondaryBackground.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            ComposeTextView.TitleTextView(
                text = user.name,
                fontSize = 16.sp,
                textColor = customColors.titleTextColor
            )
            if (!user.bio.isNullOrEmpty()) {
                ComposeTextView.TextView(
                    text = user.bio,
                    fontSize = 12.sp,
                    textColor = customColors.hintTextColor,
                    maxLines = 1
                )
            }
        }

        if (actionLabel != null) {
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (actionLabel == stringResource(R.string.unfollow)) 
                        customColors.fadedBackground else customColors.secondaryBackground,
                    contentColor = if (actionLabel == stringResource(R.string.unfollow)) 
                        customColors.titleTextColor else customColors.primaryButtonText
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                ComposeTextView.TextView(
                    text = actionLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textColor = if (actionLabel == stringResource(R.string.unfollow)) 
                        customColors.titleTextColor else customColors.primaryButtonText
                )
            }
        }
    }
}
