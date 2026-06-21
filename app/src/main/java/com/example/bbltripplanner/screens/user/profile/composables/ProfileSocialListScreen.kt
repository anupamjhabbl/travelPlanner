package com.example.bbltripplanner.screens.user.profile.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

@Composable
fun ProfileFollowersPage(
    viewModel: ProfileFollowersViewModel,
    searchQuery: String,
    onFollowClick: (User) -> Unit = { viewModel.followUser(it.id) }
) {
    val scope = rememberCoroutineScope()
    val emptyTitle = stringResource(R.string.followers_empty_heading)
    val emptyContent = stringResource(R.string.followers_empty_content)
    val uiState by viewModel.userList.collectAsStateWithLifecycle()
    val isSelf = viewModel.isSelfProfile()

    if (uiState.isLoading) {
        ComposeViewUtils.FullScreenLoading()
    } else if (uiState.data == null || uiState.error != null) {
        val errorStrings = when (uiState.error) {
            Constants.ErrorType.NETWORK_ERROR -> Pair(stringResource(R.string.no_internet_connection), stringResource(R.string.no_internet_connection_subtitle))
            Constants.ErrorType.SERVER_ERROR -> Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
            Constants.ErrorType.NOT_FOUND -> Pair(stringResource(R.string.nothing_to_show), stringResource(R.string.noting_to_show_subtitle))
            Constants.ErrorType.NOT_AUTHORIZED -> Pair(stringResource(R.string.not_authorized_title), stringResource(R.string.not_authorized_subtitle))
            else -> Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
        }
        ComposeViewUtils.FullScreenErrorComposable(
            errorStrings = errorStrings
        )
    } else {
        val filteredUsers = remember(uiState.data, searchQuery) {
            uiState.data?.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        (it.bio?.contains(searchQuery, ignoreCase = true) == true)
            }
        }

        if (uiState.data.isNullOrEmpty()) {
            ComposeViewUtils.FullScreenErrorComposable(
                errorStrings = Pair(emptyTitle, emptyContent)
            )
        } else if (filteredUsers.isNullOrEmpty()) {
            SearchEmptyState(searchQuery)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = filteredUsers,
                    key = { it.id }
                ) { user ->
                    SocialProfileItem(
                        user = user,
                        actionLabel = if (isSelf && user.isFollowing == false) stringResource(R.string.follow) else null,
                        onActionClick = {
                            onFollowClick(user)
                        },
                        onUserClick = {
                            scope.launch {
                                CommonNavigationChannel.navigateTo(
                                    NavigationAction.Navigate(
                                        AppNavigationScreen.ProfileScreen.createRoute(user.id)
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
    viewModel: ProfileFollowingViewModel,
    searchQuery: String,
    onUnfollowClick: (String) -> Unit = { viewModel.unfollowUser(it) }
) {
    val scope = rememberCoroutineScope()
    val emptyTitle = stringResource(R.string.following_empty_heading)
    val emptyContent = stringResource(R.string.following_empty_content)
    val uiState by viewModel.userList.collectAsStateWithLifecycle()
    val isSelf = viewModel.isSelfProfile()

    if (uiState.isLoading) {
        ComposeViewUtils.FullScreenLoading()
    } else if (uiState.data == null || uiState.error != null) {
        val errorStrings = when (uiState.error) {
            Constants.ErrorType.NETWORK_ERROR -> Pair(stringResource(R.string.no_internet_connection), stringResource(R.string.no_internet_connection_subtitle))
            Constants.ErrorType.SERVER_ERROR -> Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
            Constants.ErrorType.NOT_FOUND -> Pair(stringResource(R.string.nothing_to_show), stringResource(R.string.noting_to_show_subtitle))
            Constants.ErrorType.NOT_AUTHORIZED -> Pair(stringResource(R.string.not_authorized_title), stringResource(R.string.not_authorized_subtitle))
            else -> Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
        }
        ComposeViewUtils.FullScreenErrorComposable(
            errorStrings = errorStrings
        )
    } else {
        val filteredUsers = remember(uiState.data, searchQuery) {
            uiState.data?.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        (it.bio?.contains(searchQuery, ignoreCase = true) == true)
            }
        }

        if (uiState.data.isNullOrEmpty()) {
            ComposeViewUtils.FullScreenErrorComposable(
                errorStrings = Pair(emptyTitle, emptyContent)
            )
        } else if (filteredUsers.isNullOrEmpty()) {
            SearchEmptyState(searchQuery)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = filteredUsers,
                    key = { it.id }
                ) { user ->
                    SocialProfileItem(
                        user = user,
                        actionLabel = if (isSelf) stringResource(R.string.unfollow) else null,
                        onActionClick = {
                            onUnfollowClick(user.id)
                        },
                        onUserClick = {
                            scope.launch {
                                CommonNavigationChannel.navigateTo(
                                    NavigationAction.Navigate(
                                        AppNavigationScreen.ProfileScreen.createRoute(user.id)
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
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(customColors.defaultImageCardColor)
            .border(1.dp, customColors.fadedBackground, RoundedCornerShape(16.dp))
            .clickable { onUserClick(user.id) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.profilePicture ?: "",
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(2.dp, customColors.secondaryBackground.copy(alpha = 0.3f), CircleShape)
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
                        customColors.textColor else customColors.primaryButtonText
                ),
                border = if (actionLabel == stringResource(R.string.unfollow))
                    BorderStroke(1.dp, customColors.secondaryBackground.copy(alpha = 0.4f))
                else null,
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                ComposeTextView.TextView(
                    text = actionLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textColor = if (actionLabel == stringResource(R.string.unfollow)) 
                        customColors.textColor else customColors.primaryButtonText
                )
            }
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = customColors.secondaryBackground,
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun SearchEmptyState(query: String) {
    val customColors = LocalCustomColors.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = customColors.hintTextColor,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ComposeTextView.TitleTextView(
            text = "No Results Found",
            fontSize = 18.sp,
            textColor = customColors.titleTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        ComposeTextView.TextView(
            text = "We couldn't find any match for \"$query\".",
            fontSize = 14.sp,
            textColor = customColors.hintTextColor,
            textAlign = TextAlign.Center
        )
    }
}
