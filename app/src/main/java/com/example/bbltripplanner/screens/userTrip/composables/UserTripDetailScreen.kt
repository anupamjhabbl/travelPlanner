package com.example.bbltripplanner.screens.userTrip.composables

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.CommonLifecycleAwareLaunchedEffect
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.composables.ComposeViewUtils.FullScreenLoading
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.DateUtils.toFormattedDateString
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.TripActionItem
import com.example.bbltripplanner.screens.userTrip.entity.TripActionResourceMapper
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.UserRole
import com.example.bbltripplanner.screens.userTrip.viewModels.UserTripDetailIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.UserTripDetailViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UserTripDetailScreen(
    tripId: String?
) {
    val tripActionList = TripActionResourceMapper.getTripActions()
    val viewModel: UserTripDetailViewModel = koinViewModel(parameters = { parametersOf(tripId) })
    val tripDataStatus = viewModel.userTripDetailFetchStatus.collectAsState()
    val scope = rememberCoroutineScope()
    var acceptButtonVisibility by remember {
        mutableStateOf(true)
    }
    val successMessage = stringResource(R.string.trip_accept_success)
    val acceptToSeePrompt = stringResource(R.string.accept_to_see_prpmpt)
    val context = LocalContext.current
    val customColors = LocalCustomColors.current

    CommonLifecycleAwareLaunchedEffect(viewModel.viewEffect) { viewEffect ->
        when (viewEffect) {
            is UserTripDetailIntent.ViewEffect.ShowMessage -> {
                if (viewEffect.isSuccess) {
                    acceptButtonVisibility = false
                    ComposeViewUtils.showToast(context, successMessage)
                } else {
                    ComposeViewUtils.showToast(context, viewEffect.message ?: "")
                }
            }
        }
    }

    if (tripDataStatus.value.isLoading) {
        FullScreenLoading()
    } else if(tripDataStatus.value.data == null ||  tripDataStatus.value.error != null) {
        FullScreenError(tripDataStatus.value.error)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(customColors.primaryBackground)
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                UserTripDetailToolbar()

                Spacer(Modifier.height(16.dp))

                TripSummarySection(tripDataStatus.value.data, acceptButtonVisibility) { tripId ->
                    viewModel.processEvent(UserTripDetailIntent.ViewEvent.AcceptInvitation(tripId))
                }

                Spacer(Modifier.height(12.dp))

                ComposeTextView.TitleTextView(
                    text = "Trip Dashboard",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontSize = 18.sp,
                    textColor = customColors.titleTextColor
                )

                Spacer(Modifier.height(12.dp))

                // Modern 2x2 grid for action shortcuts
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            ActionCard(tripActionList[0]) { key ->
                                scope.launch {
                                    takeAction(key, tripDataStatus.value.data) {
                                        Toast.makeText(context, acceptToSeePrompt, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            ActionCard(tripActionList[1]) { key ->
                                scope.launch {
                                    takeAction(key, tripDataStatus.value.data) {
                                        Toast.makeText(context, acceptToSeePrompt, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            ActionCard(tripActionList[2]) { key ->
                                scope.launch {
                                    takeAction(key, tripDataStatus.value.data) {
                                        Toast.makeText(context, acceptToSeePrompt, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            ActionCard(tripActionList[3]) { key ->
                                scope.launch {
                                    takeAction(key, tripDataStatus.value.data) {
                                        Toast.makeText(context, acceptToSeePrompt, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun FullScreenError(error: String?) {
    val errorStrings = when (error) {
        Constants.ErrorType.NETWORK_ERROR -> Pair(stringResource(R.string.no_internet_connection), stringResource(R.string.no_internet_connection_subtitle))
        Constants.ErrorType.SERVER_ERROR -> Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
        Constants.ErrorType.NOT_FOUND -> Pair(stringResource(R.string.nothing_to_show), stringResource(R.string.noting_to_show_subtitle))
        else -> Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
    }
    ComposeViewUtils.FullScreenErrorComposable(errorStrings)
}

@Composable
private fun TripSummarySection(
    userTripData: TripData?,
    acceptButtonVisibility: Boolean,
    onAccept: (String) -> Unit
) {
    val shareMessage = stringResource(R.string.share_message)
    val context = LocalContext.current
    if (userTripData == null) {
        return
    }
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = customColors.fadedBackground.copy(alpha = 0.35f)
        ),
        border = BorderStroke(1.dp, customColors.defaultImageCardColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ComposeTextView.TitleTextView(
                    text = userTripData.tripName,
                    fontSize = 24.sp,
                    textColor = customColors.titleTextColor
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color = customColors.secondaryBackground.copy(alpha = 0.12f), CircleShape)
                        .clickable {
                            shareDeepLink(context, shareMessage, userTripData.tripId)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Share,
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Share Trip",
                        tint = customColors.secondaryBackground
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    TripSummaryDetailItem(
                        Icons.Default.DateRange,
                        "${userTripData.startDate.toFormattedDateString()} - ${userTripData.endDate.toFormattedDateString()}"
                    )

                    Spacer(Modifier.height(10.dp))

                    TripSummaryDetailItem(
                        Icons.Default.LocationOn,
                        userTripData.whereTo?.displayName
                            ?: userTripData.whereTo?.address?.name?.plus(", ")?.plus(userTripData.whereTo.address.city)
                            ?: ""
                    )

                    Spacer(Modifier.height(10.dp))

                    TripSummaryDetailItem(Icons.Filled.Face, userTripData.visibility.value)
                }

                if (acceptButtonVisibility && userTripData.acceptanceNeeded) {
                    Box(
                        modifier = Modifier
                            .background(
                                customColors.secondaryBackground,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                userTripData.tripId?.let { onAccept(it) }
                            }
                            .padding(16.dp, 10.dp)
                    ) {
                        ComposeTextView.TextView(
                            text = stringResource(R.string.accept),
                            textColor = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (userTripData.invitedMembers.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OverlappingAvatars(members = userTripData.invitedMembers)
                        Spacer(modifier = Modifier.width(8.dp))
                        ComposeTextView.TextView(
                            text = "${userTripData.invitedMembers.size} planned mates",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textColor = customColors.textColor
                        )
                    }
                } else {
                    ComposeTextView.TextView(
                        text = "Solo Trip",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textColor = customColors.textColor
                    )
                }

                if (userTripData.role == UserRole.ADMIN) {
                    Box(
                        modifier = Modifier
                            .background(
                                customColors.secondaryBackground.copy(alpha = 0.12f),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                scope.launch {
                                    userTripData.tripId?.let {
                                        CommonNavigationChannel.navigateTo(
                                            NavigationAction.Navigate(
                                                AppNavigationScreen.EditTripScreen.createRoute(it)
                                            )
                                        )
                                    }
                                }
                            }
                            .padding(14.dp, 8.dp)
                    ) {
                        ComposeTextView.TextView(
                            text = stringResource(R.string.edit_details),
                            textColor = customColors.secondaryBackground,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TripSummaryDetailItem(
    icon: ImageVector,
    text: String
) {
    val customColors = LocalCustomColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = customColors.secondaryBackground
        )

        Spacer(Modifier.width(8.dp))

        ComposeTextView.TextView(
            text = text,
            fontSize = 15.sp,
            textColor = customColors.textColor
        )
    }
}

@Composable
private fun UserTripDetailToolbar() {
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 16.dp, 16.dp, 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(customColors.secondaryBackground.copy(alpha = 0.12f))
                .clickable {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.NavigateUp
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = customColors.secondaryBackground,
                modifier = Modifier.size(20.dp)
            )
        }

        ComposeTextView.TextView(
            text = stringResource(R.string.see_trips),
            textColor = customColors.secondaryBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                scope.launch {
                    CommonNavigationChannel.navigateTo(
                        NavigationAction.Navigate(
                            AppNavigationScreen.UserTripsScreen.route
                        )
                    )
                }
            }
        )
    }
}

@Composable
private fun ActionCard(
    item: TripActionItem,
    onClick: (key: String) -> Unit
) {
    val customColors = LocalCustomColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(customColors.fadedBackground.copy(alpha = 0.35f))
            .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(20.dp))
            .clickable { onClick(item.key) }
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(customColors.secondaryBackground.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            ComposeImageView.ImageViewWitDrawableId(
                imageId = item.vectorId,
                modifier = Modifier.size(24.dp),
                contentDescription = stringResource(id = item.title)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ComposeTextView.TextView(
            text = stringResource(id = item.title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textColor = customColors.titleTextColor
        )

        Spacer(modifier = Modifier.height(10.dp))

        ComposeTextView.TextView(
            text = stringResource(id = item.subTitle),
            fontSize = 13.sp,
            textColor = customColors.hintTextColor,
            maxLines = 2
        )
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
                        imageVector = Icons.Default.Face,
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
                androidx.compose.material3.Text(
                    text = "+${members.size - maxVisible}",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun shareDeepLink(context: Context, message: String, tripId: String?) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Check out this product!")
        putExtra(Intent.EXTRA_TEXT, "$message ${getDeeplinkUrl(tripId)}")
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share deep link via"))
}

fun getDeeplinkUrl(tripId: String?): String {
    return if (tripId.isNullOrEmpty()) {
        Constants.TRIP_PLANNER_DEEPLINK
    } else {
        "${Constants.TRIP_PLANNER_DEEPLINK}/$tripId"
    }
}

private suspend fun takeAction(
    key: String,
    trip: TripData?,
    acceptToSeePrompt: () -> Unit
) {
    if (trip?.tripId != null) {
        when (key) {
            Constants.TripDetailScreen.ATTACHMENTS -> {
                navigateToIfAllowed(
                    AppNavigationScreen.TripGalleryNavEntry.createRoute(trip.tripId),
                    !trip.acceptanceNeeded && trip.role != UserRole.PUBLIC,
                    acceptToSeePrompt
                )
            }

            Constants.TripDetailScreen.GROUP -> {
                navigateToIfAllowed(
                    AppNavigationScreen.TripGroupScreen.createRoute(trip.tripId, trip.role == UserRole.ADMIN),
                    true,
                    acceptToSeePrompt
                )
            }

            Constants.TripDetailScreen.ITINERARY -> {
                navigateToIfAllowed(
                    AppNavigationScreen.ItineraryListView.createRoute(trip.tripId),
                    !trip.acceptanceNeeded && trip.role != UserRole.PUBLIC,
                    acceptToSeePrompt
                )
            }

            Constants.TripDetailScreen.EXPENSES -> {
                navigateToIfAllowed(
                    AppNavigationScreen.ExpenseScreen.createRoute(trip.tripId),
                    !trip.acceptanceNeeded && trip.role != UserRole.PUBLIC,
                    acceptToSeePrompt
                )
            }
        }
    }
}

private suspend fun navigateToIfAllowed(
    route: String,
    allowed: Boolean,
    acceptToSeePrompt: () -> Unit
) {
    if (allowed) {
        CommonNavigationChannel.navigateTo(
            NavigationAction.Navigate(
                route
            )
        )
    } else {
        acceptToSeePrompt()
    }
}