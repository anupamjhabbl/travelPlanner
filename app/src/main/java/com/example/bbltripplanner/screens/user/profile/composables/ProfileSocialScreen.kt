package com.example.bbltripplanner.screens.user.profile.composables

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.user.profile.entity.ProfileSocialScreens
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileFollowersViewModel
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileFollowingViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileSocialScreen(
    pageId: String?,
    userId: String?
) {
    val followersPageIndex = 0
    val followingPageIndex = 1
    val initialPage = when (pageId) {
        ProfileSocialScreens.FOLLOWERS.value -> followersPageIndex
        ProfileSocialScreens.FOLLOWING.value -> followingPageIndex
        else -> followersPageIndex
    }
    val pagerState = rememberPagerState(initialPage)
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current

    // Fetch view models at the parent level to access counts and share search state
    val followersViewModel: ProfileFollowersViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val followingViewModel: ProfileFollowingViewModel = koinViewModel(parameters = { parametersOf(userId) })

    val followersState by followersViewModel.userList.collectAsStateWithLifecycle()
    val followingState by followingViewModel.userList.collectAsStateWithLifecycle()

    val followerCount = followersState.data?.size
    val followingCount = followingState.data?.size

    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(customColors.secondaryBackground.copy(alpha = 0.1f))
                    .clickable {
                        scope.launch {
                            CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = customColors.secondaryBackground,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            ComposeTextView.TitleTextView(
                text = stringResource(R.string.contacts),
                fontSize = 20.sp,
                textColor = customColors.titleTextColor
            )
        }

        Spacer(Modifier.height(16.dp))

        // Custom Segmented Pill Tab Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(customColors.fadedBackground)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = listOf(
                Pair(stringResource(R.string.followers), followerCount),
                Pair(stringResource(R.string.followings), followingCount)
            )

            tabs.forEachIndexed { index, (label, count) ->
                val isSelected = pagerState.currentPage == index
                val tabBgColor by animateColorAsState(
                    targetValue = if (isSelected) customColors.secondaryBackground else Color.Transparent,
                    label = "tabBgColor"
                )
                val tabTextColor by animateColorAsState(
                    targetValue = if (isSelected) customColors.primaryButtonText else customColors.hintTextColor,
                    label = "tabTextColor"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(tabBgColor)
                        .clickable {
                            if (pagerState.currentPage != index) {
                                scope.launch {
                                    pagerState.scrollToPage(index)
                                }
                            }
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val displayText = if (count != null) "$label ($count)" else label
                    ComposeTextView.TitleTextView(
                        text = displayText,
                        fontSize = 14.sp,
                        textColor = tabTextColor
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Premium Capsule Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                ComposeTextView.TextView(
                    text = stringResource(R.string.search_by_name),
                    textColor = customColors.hintTextColor,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = customColors.hintTextColor,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = customColors.hintTextColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = customColors.fadedBackground,
                unfocusedContainerColor = customColors.fadedBackground,
                disabledContainerColor = customColors.fadedBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = customColors.textColor,
                unfocusedTextColor = customColors.textColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        )

        Spacer(Modifier.height(16.dp))

        // Tab Content
        HorizontalPager(
            count = 2,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { index ->
            when (index) {
                followersPageIndex -> {
                    ProfileFollowersPage(
                        viewModel = followersViewModel,
                        searchQuery = searchQuery,
                        onFollowClick = { user ->
                            followersViewModel.followUser(user.id)
                            followingViewModel.addFollowedUserLocal(user)
                        }
                    )
                }
                followingPageIndex -> {
                    ProfileFollowingPage(
                        viewModel = followingViewModel,
                        searchQuery = searchQuery,
                        onUnfollowClick = { userId ->
                            followingViewModel.unfollowUser(userId)
                            followersViewModel.updateFollowStatusLocal(userId, isFollowing = false)
                        }
                    )
                }
                else -> {
                    ProfileFollowersPage(
                        viewModel = followersViewModel,
                        searchQuery = searchQuery,
                        onFollowClick = { user ->
                            followersViewModel.followUser(user.id)
                            followingViewModel.addFollowedUserLocal(user)
                        }
                    )
                }
            }
        }
    }
}