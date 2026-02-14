package com.example.bbltripplanner.screens.user.profile.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.screens.user.profile.entity.ProfileSocialScreens
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileSocialScreen(
    pageId: String?,
    userId: String?
) {
    val followersPageIndex = 0
    val followingPageIndex = 1
    val initialPage = when (pageId) {
        ProfileSocialScreens.FOLLOWERS.value -> {
            followersPageIndex
        }
        ProfileSocialScreens.FOLLOWING.value -> {
            followingPageIndex
        }
        else -> {
            followersPageIndex
        }
    }
    val pagerState = rememberPagerState(initialPage)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            divider = {},
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 2.dp,
                    color = LocalCustomColors.current.secondaryBackground
                )
            }
        ) {
            Tab(
                modifier = Modifier.padding(16.dp, 0.dp),
                selected = pagerState.currentPage == followersPageIndex,
                onClick = {
                    if (pagerState.currentPage != followersPageIndex) {
                        scope.launch {
                            pagerState.scrollToPage(followersPageIndex)
                        }
                    }
                },
                text = {
                    ComposeTextView.TitleTextView(
                        text = stringResource(R.string.followers),
                        textColor = if (pagerState.currentPage == followersPageIndex) LocalCustomColors.current.secondaryBackground else LocalCustomColors.current.hintTextColor
                    )
                }
            )

            Tab(
                selected = pagerState.currentPage == followingPageIndex,
                onClick = {
                    if (pagerState.currentPage != followingPageIndex) {
                        scope.launch {
                            pagerState.scrollToPage(followingPageIndex)
                        }
                    }
                },
                text = {
                    ComposeTextView.TitleTextView(
                        text = stringResource(R.string.followings),
                        textColor = if (pagerState.currentPage == followingPageIndex) LocalCustomColors.current.secondaryBackground else LocalCustomColors.current.hintTextColor
                    )
                }
            )
        }

        Spacer(Modifier.height(48.dp))

        HorizontalPager(
            count = 2,
            state = pagerState
        ) { index ->
            when (index) {
                followersPageIndex -> {
                    ProfileFollowersPage(userId)
                }
                followingPageIndex -> {
                    ProfileFollowingPage(userId)
                }
                else -> {
                    ProfileFollowersPage(userId)
                }
            }
        }
    }
}