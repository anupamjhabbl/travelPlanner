package com.example.bbltripplanner.screens.user.profile.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.user.profile.viewModels.BlockedUsersViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun BlockedUsersScreen() {
    val customColors = LocalCustomColors.current
    val viewModel: BlockedUsersViewModel = koinViewModel()
    val uiState by viewModel.blockedUsers.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
    ) {
        BlockedUsersToolbar {
            scope.launch {
                CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
            }
        }

        if (uiState.isLoading) {
            ComposeViewUtils.FullScreenLoading()
        } else if (uiState.error != null) {
            ComposeViewUtils.FullScreenErrorComposable(
                errorStrings = Pair(stringResource(R.string.server_error), uiState.error ?: "")
            )
        } else {
            if (uiState.data.isNullOrEmpty()) {
                ComposeViewUtils.FullScreenErrorComposable(
                    errorStrings = Pair(
                        stringResource(R.string.no_blocked_users_title),
                        stringResource(R.string.no_blocked_users_subtitle)
                    )
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp, 8.dp)
                ) {
                    items(
                        items = uiState.data as List<User>,
                        key = { it.id }
                    ) { user ->
                        SocialProfileItem(
                            user = user,
                            actionLabel = stringResource(R.string.unblock),
                            onActionClick = {
                                viewModel.unblockUser(user.id)
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
}

@Composable
private fun BlockedUsersToolbar(onBackClick: () -> Unit) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = customColors.secondaryBackground
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        ComposeTextView.TitleTextView(
            text = stringResource(R.string.blocked_users),
            fontSize = 20.sp,
            textColor = customColors.secondaryBackground
        )
    }
}
