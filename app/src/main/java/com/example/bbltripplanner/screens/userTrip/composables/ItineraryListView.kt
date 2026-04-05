package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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

@Composable
fun ItineraryListView(
    viewModel: ItineraryViewModel,
    tripId: String?
) {
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    val itineraryStatus by viewModel.itineraryStatus.collectAsState()
    val errorMessage = stringResource(R.string.generic_error)

    LaunchedEffect(tripId) {
        tripId?.let {
            viewModel.processEvent(ItineraryIntent.ViewEvent.FetchItinerary(it))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.module_16),
                    vertical = dimensionResource(id = R.dimen.module_8)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.module_24))
                        .clickable {
                            scope.launch {
                                CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                            }
                        },
                    tint = customColors.secondaryBackground
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.module_16)))

                ComposeTextView.TitleTextView(
                    text = stringResource(id = R.string.choose_date),
                    fontSize = 18.sp,
                    textColor = customColors.secondaryBackground
                )
            }
        }

        if (itineraryStatus.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = customColors.secondaryBackground)
            }
        } else if (itineraryStatus.error != null) {
            ComposeViewUtils.FullScreenErrorComposable(Pair(errorMessage, itineraryStatus.error ?: ""))
        } else {
            val itinerary = itineraryStatus.data
            val itineraryDays = itinerary?.itineraryDayList ?: emptyList()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.module_16))
            ) {
                item {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.module_24)))

                    ComposeTextView.TitleTextView(
                        text = itinerary?.itineraryName ?: stringResource(id = R.string.your_itineary),
                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.module_10)))

                    ComposeTextView.TextView(
                        text = itinerary?.itinerarySummary ?: stringResource(id = R.string.select_date_summary),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.module_24)))
                }

                itemsIndexed(itineraryDays) { index, day ->
                    DayItem(
                        day = day,
                        dayNumber = index + 1,
                        onClick = {
                            scope.launch {
                                CommonNavigationChannel.navigateTo(
                                    NavigationAction.Navigate(
                                        AppNavigationScreen.ItineraryMapViewScreen.createRoute(day.date.toString())
                                    )
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.module_16)))
                }
            }
        }
    }
}

@Composable
fun DayItem(
    day: ItineraryDay,
    dayNumber: Int,
    onClick: () -> Unit
) {
    val customColors = LocalCustomColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.module_20)))
            .background(customColors.deepPurpleGlow)
            .clickable { onClick() }
            .padding(dimensionResource(id = R.dimen.module_12)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ComposeImageView.ImageViewWithUrl(
            imageURI = day.imageUrl,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.module_90))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.module_12))),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.module_16)))

        Column(modifier = Modifier.weight(1f)) {
            ComposeTextView.TextView(
                text = stringResource(id = R.string.from_to_day, dayNumber, DateTimeUtils.formatLongToDate(day.date)),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textColor = customColors.titleTextColor
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.module_6)))

            ComposeTextView.TextView(
                text = stringResource(id = R.string.curated_place_count, day.spotCount),
                fontSize = 16.sp
            )
        }
    }
}
