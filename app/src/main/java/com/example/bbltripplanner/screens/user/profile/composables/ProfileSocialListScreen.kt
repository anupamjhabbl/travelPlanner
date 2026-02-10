package com.example.bbltripplanner.screens.user.profile.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileFollowersViewModel
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileFollowingViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProfileFollowersPage(
    navController: NavController,
    userId: String?
) {
    val emptyTitle = stringResource(R.string.followers_empty_heading)
    val emptyContent = stringResource(R.string.followers_empty_content)
    val viewModel: ProfileFollowersViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val uiState by viewModel.userList.collectAsStateWithLifecycle()

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
                        onUserClick = {
                            navController.navigate(
                                AppNavigationScreen.ProfileScreen.createRoute(
                                    user.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileFollowingPage(
    navController: NavController,
    userId: String?
) {
    val emptyTitle = stringResource(R.string.following_empty_heading)
    val emptyContent = stringResource(R.string.following_empty_content)
    val viewModel: ProfileFollowingViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val uiState by viewModel.userList.collectAsStateWithLifecycle()

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
                        onUserClick = {
                            navController.navigate(
                                AppNavigationScreen.ProfileScreen.createRoute(
                                    user.id
                                )
                            )
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
    onUserClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, LocalCustomColors.current.secondaryBackground, RoundedCornerShape(16.dp))
            .background(LocalCustomColors.current.primaryBackground)
            .clickable { onUserClick(user.id) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = user.profilePicture ?: "",
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(LocalCustomColors.current.secondaryBackground),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            ComposeTextView.TitleTextView(
                text = user.name,
                textColor = LocalCustomColors.current.titleTextColor
            )
        }
    }
}
