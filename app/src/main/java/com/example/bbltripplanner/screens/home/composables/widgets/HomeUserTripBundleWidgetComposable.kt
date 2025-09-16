package com.example.bbltripplanner.screens.home.composables.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget
import com.example.bbltripplanner.screens.home.entities.UserTripWidgetItem
import com.example.bbltripplanner.screens.vault.entity.VaultScreens
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeUserTripBundleWidgetComposable(
    navController: NavController,
    widget: HomeCxeWidget.UserTripBundleWidget,
    user: User?
) {
    val widgetItemList = widget.data.widgetList
    val pagerState = rememberPagerState()

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
                        user?.let {
                            navController.navigate(
                                AppNavigationScreen.VaultScreen.createRoute(
                                    VaultScreens.TRIPS.value, it.id
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
            UserTripWidgetItem(widgetItemList[pageNo]) { tripId ->
                navController.navigate(AppNavigationScreen.UserTripDetailScreen.createRoute(tripId))
            }
        }
    }
}

@Composable
fun UserTripWidgetItem(
    userTripWidgetItem: UserTripWidgetItem,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
            .border(1.dp, LocalCustomColors.current.defaultImageCardColor, RoundedCornerShape(12.dp))
            .padding(8.dp)
            .clickable {
                onClick(userTripWidgetItem.userTripId)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        ComposeImageView.ImageViewWithUrl(
            imageURI = userTripWidgetItem.tripImages.firstOrNull() ?: "",
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ComposeTextView.TitleTextView(
                    text = userTripWidgetItem.tripName,
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp
                )

                ComposeTextView.TextView(
                    text = "Share Stories",
                    fontSize = 14.sp,
                    textColor = LocalCustomColors.current.secondaryBackground,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        onClick(userTripWidgetItem.userTripId)
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                userTripWidgetItem.tripProfile?.profileImage?.let {
                    ComposeImageView.CircularImageView(
                        imageURI = it,
                        diameter = 15.dp
                    )
                }

                Spacer(Modifier.width(6.dp))

                ComposeTextView.TextView(
                    text = userTripWidgetItem.tripProfile?.profileName ?: ""
                )
            }

            ComposeTextView.TextView(
                text = "${userTripWidgetItem.tripLocation?.cityName} - ${userTripWidgetItem.date}"
            )
        }
    }
}
