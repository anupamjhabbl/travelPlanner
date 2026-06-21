package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.utils.DateTimeUtils
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryDay
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ItineraryListView(
    tripId: String?,
) {
    val viewModel: ItineraryViewModel = koinViewModel(parameters = { parametersOf(tripId) })
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    val itineraryStatus by viewModel.itineraryStatus.collectAsStateWithLifecycle()
    var addItineraryDialogVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(itineraryStatus) {
        if (!itineraryStatus.isLoading && (itineraryStatus.data == null || itineraryStatus.data!!.itineraryList.isEmpty()) && itineraryStatus.error == null) {
            addItineraryDialogVisibility = true
        }
    }

    if (addItineraryDialogVisibility) {
        ComposeViewUtils.ConfirmationDialog(
            title = stringResource(R.string.add_itinerary_title),
            message = stringResource(R.string.add_itinerary_message),
            confirmButtonText = stringResource(R.string.add),
            dismissButtonText = stringResource(R.string.cancel),
            onConfirm = {
                addItineraryDialogVisibility = false
                tripId?.let {
                    viewModel.processEvent(ItineraryIntent.ViewEvent.GenerateItinerary(tripId))
                }
            },
            onDismiss = {
                addItineraryDialogVisibility = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
    ) {
        // Updated Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 48.dp, 16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    scope.launch { CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp) }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = customColors.secondaryBackground
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            ComposeTextView.TitleTextView(
                text = stringResource(id = R.string.itinerary),
                fontSize = 24.sp
            )
        }

        if (itineraryStatus.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = customColors.secondaryBackground)
            }
        } else if (itineraryStatus.error != null) {
            val errorStrings = when (itineraryStatus.error) {
                "NETWORK_ERROR" -> Pair(stringResource(R.string.no_internet_connection), stringResource(R.string.no_internet_connection_subtitle))
                "SERVER_ERROR" -> Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
                "NOT_FOUND" -> Pair(stringResource(R.string.nothing_to_show), stringResource(R.string.noting_to_show_subtitle))
                else -> Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
            }
            ComposeViewUtils.FullScreenErrorComposable(errorStrings)
        } else {
            val itinerary = itineraryStatus.data
            val itineraryDays = itinerary?.itineraryList ?: emptyList()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    ComposeTextView.TitleTextView(
                        text = itinerary?.itineraryName ?: stringResource(id = R.string.your_itineary),
                        fontSize = 20.sp,
                        textColor = customColors.secondaryBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ComposeTextView.TextView(
                        text = itinerary?.itinerarySummary ?: stringResource(id = R.string.select_date_summary),
                        fontSize = 14.sp,
                        textColor = customColors.hintTextColor
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                itemsIndexed(itineraryDays) { index, day ->
                    DayCard(
                        day = day,
                        dayNumber = index + 1,
                        isLast = index == itineraryDays.size - 1,
                        onClick = {
                            scope.launch {
                                day.itineraryId.let {
                                    CommonNavigationChannel.navigateTo(
                                        NavigationAction.Navigate(
                                            AppNavigationScreen.ItineraryMapViewScreen.createRoute(it)
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun DayCard(
    day: ItineraryDay,
    dayNumber: Int,
    isLast: Boolean,
    onClick: () -> Unit
) {
    val customColors = LocalCustomColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Timeline Dot and Line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(customColors.primaryButtonBg, CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .weight(1f)
                        .background(customColors.fadedBackground)
                )
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 24.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(16.dp))
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = customColors.defaultImageCardColor.copy(alpha = 0.35f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ComposeImageView.ImageViewWithUrl(
                    imageURI = day.imageUrl ?: "",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    ComposeTextView.TextView(
                        text = "Day $dayNumber",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textColor = customColors.primaryButtonBg
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ComposeTextView.TextView(
                        text = DateTimeUtils.formatLongToDate(day.date),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textColor = customColors.titleTextColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ComposeTextView.TextView(
                        text = "Tap to view schedule",
                        fontSize = 12.sp,
                        textColor = customColors.hintTextColor
                    )
                }
            }
        }
    }
}
