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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.MenuItems
import com.example.bbltripplanner.common.entity.User
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
            .padding(16.dp, 16.dp, 16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(customColors.secondaryBackground.copy(alpha = 0.12f))
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

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(R.string.your_trips),
            style = MaterialTheme.typography.titleMedium.copy(
                color = customColors.titleTextColor,
                fontWeight = FontWeight.Bold
            ),
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
            .clip(RoundedCornerShape(20.dp))
            .background(customColors.fadedBackground.copy(alpha = 0.35f))
            .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(20.dp))
            .clickable { onCardClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(customColors.fadedBackground),
            contentAlignment = Alignment.Center
        ) {
            val imageUrl = trip.invitedMembers.firstOrNull()?.profilePicture
            if (!imageUrl.isNullOrEmpty()) {
                ComposeImageView.ImageViewWithUrl(
                    imageURI = imageUrl,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = null,
                    tint = customColors.secondaryBackground.copy(alpha = 0.6f),
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(Modifier.width(16.dp))

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
                    fontSize = 16.sp,
                    textColor = customColors.titleTextColor
                )

                ComposeViewUtils.Menu(
                    menuItems = menuItems,
                    onItemClick = { item ->
                        when (item) {
                            MenuItems.MyProfileMenuItem.EDIT.value -> onEditClick()
                            MenuItems.MyProfileMenuItem.SHARE.value -> onShareClick()
                        }
                    },
                    boxSize = 28.dp,
                    iconSize = 18.dp
                )
            }

            Spacer(Modifier.height(10.dp))

            // Overlapping mates section
            if (trip.invitedMembers.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OverlappingAvatars(members = trip.invitedMembers)

                    Spacer(Modifier.width(8.dp))

                    val extraCount = trip.invitedMembers.size - 1
                    val membersText = if (extraCount > 0) {
                        "${trip.invitedMembers.firstOrNull()?.name} +$extraCount mates"
                    } else {
                        trip.invitedMembers.firstOrNull()?.name ?: stringResource(R.string.personal_trip)
                    }

                    ComposeTextView.TextView(
                        text = membersText,
                        fontSize = 12.sp,
                        textColor = customColors.textColor.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(10.dp))
            } else {
                ComposeTextView.TextView(
                    text = stringResource(R.string.personal_trip),
                    fontSize = 12.sp,
                    textColor = customColors.textColor.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(10.dp))
            }

            val formattedDate = DateUtils.formatTripDateRange(trip.startDate, trip.endDate)
            val locationName = trip.whereTo?.displayName ?: stringResource(R.string.unknown_location)

            ComposeTextView.TextView(
                text = "$formattedDate • $locationName",
                fontSize = 11.sp,
                textColor = customColors.hintTextColor,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun OverlappingAvatars(members: List<User>, modifier: Modifier = Modifier) {
    val customColors = LocalCustomColors.current
    val maxVisible = 3
    Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
        members.take(maxVisible).forEachIndexed { index, member ->
            Box(
                modifier = Modifier
                    .padding(start = (index * 14).dp)
                    .size(22.dp)
                    .border(1.5.dp, customColors.primaryBackground, CircleShape)
                    .clip(CircleShape)
                    .background(customColors.fadedBackground),
                contentAlignment = Alignment.Center
            ) {
                if (!member.profilePicture.isNullOrEmpty()) {
                    ComposeImageView.CircularImageView(
                        imageURI = member.profilePicture,
                        diameter = 22.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        tint = customColors.secondaryBackground,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
        if (members.size > maxVisible) {
            Box(
                modifier = Modifier
                    .padding(start = (maxVisible * 14).dp)
                    .size(22.dp)
                    .border(1.5.dp, customColors.primaryBackground, CircleShape)
                    .clip(CircleShape)
                    .background(customColors.secondaryBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+${members.size - maxVisible}",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
