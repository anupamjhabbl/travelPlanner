package com.example.bbltripplanner.screens.vault.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.MenuItems
import com.example.bbltripplanner.common.utils.DateUtils
import com.example.bbltripplanner.common.utils.shareDeepLinkOfTrip
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.vault.viewModels.UserTripsViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserTripsScreen() {
    val viewModel: UserTripsViewModel = koinViewModel()
    val tripsStatus by viewModel.userTripsStatus.collectAsState()
    val customColors = LocalCustomColors.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val shareMessage = stringResource(R.string.share_message)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
    ) {
        UserTripsToolbar()

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when {
                tripsStatus.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = customColors.secondaryBackground
                    )
                }

                tripsStatus.error != null -> {
                    ComposeTextView.TextView(
                        text = tripsStatus.error ?: stringResource(R.string.generic_error),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                tripsStatus.data != null -> {
                    val trips = tripsStatus.data!!
                    if (trips.isEmpty()) {
                        ComposeTextView.TextView(
                            text = stringResource(R.string.no_trips),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(trips) { trip ->
                                TripListItem(
                                    trip = trip,
                                    onCardClick = {
                                        scope.launch {
                                            CommonNavigationChannel.navigateTo(
                                                NavigationAction.Navigate(
                                                    AppNavigationScreen.UserTripDetailScreen.createRoute(
                                                        trip.tripId ?: ""
                                                    )
                                                )
                                            )
                                        }
                                    },
                                    onEditClick = {
                                        scope.launch {
                                            CommonNavigationChannel.navigateTo(
                                                NavigationAction.Navigate(
                                                    AppNavigationScreen.EditTripScreen.createRoute(
                                                        trip.tripId ?: ""
                                                    )
                                                )
                                            )
                                        }
                                    },
                                    onShareClick = {
                                        context.shareDeepLinkOfTrip(
                                            shareMessage,
                                            trip.tripId
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserTripsToolbar() {
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color = customColors.secondaryBackground, CircleShape)
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                    }
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = customColors.primaryBackground
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        ComposeTextView.TitleTextView(
            text = stringResource(R.string.your_trips),
            fontSize = 20.sp
        )
    }
}

@Composable
fun TripListItem(
    trip: TripData,
    onCardClick: () -> Unit,
    onEditClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val customColors = LocalCustomColors.current
    val menuItems = listOf(MenuItems.MyProfileMenuItem.EDIT.value, MenuItems.MyProfileMenuItem.SHARE.value)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(customColors.fadedBackground.copy(alpha = 0.3f))
            .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(12.dp))
            .clickable { onCardClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(customColors.fadedBackground),
            contentAlignment = Alignment.Center
        ) {
            val imageUrl = trip.invitedMembers.firstOrNull()?.profilePicture
            if (imageUrl != null) {
                ComposeImageView.ImageViewWithUrl(
                    imageURI = imageUrl,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = null,
                    tint = customColors.primaryBackground,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ComposeTextView.TitleTextView(
                    text = trip.tripName,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp
                )

                ComposeViewUtils.Menu(
                    menuItems = menuItems,
                    onItemClick = { item ->
                        when (item) {
                            MenuItems.MyProfileMenuItem.EDIT.value -> onEditClick()
                            MenuItems.MyProfileMenuItem.SHARE.value -> onShareClick()
                        }
                    },
                    boxSize = 30.dp,
                    iconSize = 20.dp
                )
            }

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val member = trip.invitedMembers.firstOrNull()
                ComposeImageView.CircularImageView(
                    imageURI = member?.profilePicture ?: "",
                    diameter = 16.dp,
                    modifier = Modifier.background(customColors.secondaryBackground, CircleShape)
                )

                Spacer(Modifier.width(6.dp))

                ComposeTextView.TextView(
                    text = member?.name ?: stringResource(R.string.personal_trip),
                    fontSize = 12.sp,
                    textColor = customColors.textColor.copy(alpha = 0.8f)
                )
            }

            Spacer(Modifier.height(4.dp))

            val formattedDate = DateUtils.formatTripDateRange(trip.startDate, trip.endDate)
            val locationName = trip.whereTo?.displayName ?: stringResource(R.string.unknown_location)

            ComposeTextView.TextView(
                text = "$formattedDate, $locationName",
                fontSize = 12.sp,
                textColor = customColors.hintTextColor
            )
        }
    }
}
