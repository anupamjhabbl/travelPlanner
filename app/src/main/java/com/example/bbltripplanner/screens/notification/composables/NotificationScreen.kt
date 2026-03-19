package com.example.bbltripplanner.screens.notification.composables

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.composables.ToolBarView
import com.example.bbltripplanner.common.utils.DateUtils
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.notification.model.NotificationModel
import com.example.bbltripplanner.screens.notification.viewModels.NotificationViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotificationScreen() {
    val scope = rememberCoroutineScope()
    val notificationViewModel: NotificationViewModel = koinViewModel()
    val notificationFetchStatus by notificationViewModel.notificationsFetchStatus.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ToolBarView.SimpleToolbarWithBackButton(
            stringResource(R.string.notifications)
        ) {
            scope.launch {
                CommonNavigationChannel.navigateTo(
                    NavigationAction.NavigateUp
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
                .background(LocalCustomColors.current.primaryBackground)
        ) {
            if (notificationFetchStatus.isLoading) {
                ComposeViewUtils.FullScreenLoading()
            } else if (notificationFetchStatus.error != null || notificationFetchStatus.data.isNullOrEmpty()) {
                NoNotificationsView {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(
                                AppNavigationScreen.HomeScreen.route
                            )
                        )
                    }
                }
            } else {
                notificationFetchStatus.data?.let {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                            .padding(16.dp, 8.dp)
                    ) {
                        items(it) { notification ->
                            Spacer(Modifier.height(8.dp))

                            NotificationItemView(notification)

                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoNotificationsView(onExploreClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(180.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(LocalCustomColors.current.secondaryBackground.copy(alpha = 0.05f))
            )
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(LocalCustomColors.current.secondaryBackground.copy(alpha = 0.08f))
            )

            Box(contentAlignment = Alignment.TopEnd) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = LocalCustomColors.current.secondaryBackground.copy(alpha = 0.4f)
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(LocalCustomColors.current.error, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    ComposeTextView.TextView(
                        text = "0",
                        textColor = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        ComposeTextView.TitleTextView(
            text = stringResource(R.string.no_notifications_to_show),
            fontSize = 20.sp,
            textColor = LocalCustomColors.current.secondaryBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        ComposeTextView.TextView(
            text = stringResource(R.string.no_notifications_subtitle),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        ComposeButtonView.PrimaryButtonView(
            text = stringResource(R.string.explore),
            backgroundColor = LocalCustomColors.current.secondaryBackground,
            fontSize = 16.sp,
            paddingValues = androidx.compose.foundation.layout.PaddingValues(horizontal = 32.dp, vertical = 12.dp),
            onClick = onExploreClick
        )
    }
}

@Composable
fun NotificationItemView(notification: NotificationModel) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .alpha(if (notification.isRead) 0.7f else 1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors().copy(containerColor = LocalCustomColors.current.fadedBackground)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            ComposeImageView.ImageViewWithUrl(
                imageURI = notification.iconUrl ?: "",
                modifier = Modifier.size(48.dp).padding(top = 4.dp).clip(RoundedCornerShape(8.dp)),
            )

            Spacer(Modifier.width(16.dp))

            Column {
                ComposeTextView.TitleTextView(
                    text = notification.heading,
                    fontSize = 16.sp,
                    textColor = LocalCustomColors.current.textColor
                )

                Spacer(Modifier.height(4.dp))

                ComposeTextView.TextView(
                    text = notification.message,
                    textColor = LocalCustomColors.current.textColor,
                    fontSize = 14.sp,
                    maxLines = 2,
                    minLines = 2
                )

                Spacer(Modifier.height(8.dp))

                ComposeTextView.TextView(
                    text = DateUtils.getTimeAgo(notification.date),
                    textColor = LocalCustomColors.current.textColor,
                    fontSize = 14.sp
                )
            }
        }
    }
}
