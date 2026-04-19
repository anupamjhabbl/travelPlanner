package com.example.bbltripplanner.screens.home.composables.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.shareDeepLinkOfTrip
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget
import com.example.bbltripplanner.screens.vault.composables.TripListItem
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeUserTripBundleWidgetComposable(
    widget: HomeCxeWidget.UserTripBundleWidget,
    user: User?
) {
    val context = LocalContext.current
    val shareMessage = stringResource(R.string.share_message)
    val widgetItemList = widget.data.widgetList
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    if (widgetItemList.isNullOrEmpty()) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ComposeTextView.TitleTextView(
                modifier = Modifier.weight(1f),
                text = widget.data.header?.text ?: "",
                fontSize = 18.sp,
            )

            Box(
                modifier = Modifier
                    .background(LocalCustomColors.current.secondaryBackground, RoundedCornerShape(8.dp))
                    .padding(12.dp, 4.dp)
                    .clickable {
                        scope.launch {
                            CommonNavigationChannel.navigateTo(
                                NavigationAction.Navigate(
                                    AppNavigationScreen.UserTripsScreen.route
                                )
                            )
                        }
                    }
            ) {
                ComposeTextView.TitleTextView(
                    text = widget.data.actionHeader?.text ?: "",
                    textColor = LocalCustomColors.current.primaryBackground,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        HorizontalPager(
            state = pagerState,
            count = widgetItemList.size
        ) { pageNo ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                TripListItem(
                    trip = widgetItemList[pageNo],
                    onCardClick = { ->
                        scope.launch {
                            widgetItemList[pageNo].tripId?.let {
                                CommonNavigationChannel.navigateTo(
                                    NavigationAction.Navigate(
                                        AppNavigationScreen.UserTripDetailScreen.createRoute(it)
                                    )
                                )
                            }
                        }
                    },
                    onEditClick = {
                        scope.launch {
                            widgetItemList[pageNo].tripId?.let {
                                CommonNavigationChannel.navigateTo(
                                    NavigationAction.Navigate(
                                        AppNavigationScreen.EditTripScreen.createRoute(it)
                                    )
                                )
                            }
                        }
                    },
                    onShareClick = {
                        widgetItemList[pageNo].tripId?.let {
                            context.shareDeepLinkOfTrip(shareMessage, it)
                        }
                    }
                )
            }
        }
    }
}
