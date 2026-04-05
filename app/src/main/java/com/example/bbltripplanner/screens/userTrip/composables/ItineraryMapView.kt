package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryPlace
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import kotlinx.coroutines.launch

@Composable
fun ItineraryMapView(
    viewModel: ItineraryViewModel,
    tripSelectedDate: String? = null
) {
    val itineraryStatus by viewModel.itineraryStatus.collectAsState()
    val scope = rememberCoroutineScope()

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(12.0)
            center(Point.fromLngLat(77.0958, 28.6573))
            pitch(0.0)
            bearing(0.0)
        }
    }

    val places = remember(itineraryStatus.data, tripSelectedDate) {
        val selectedDateLong = tripSelectedDate?.toLongOrNull()
        if (selectedDateLong != null) {
            itineraryStatus.data?.itineraryDayList?.find { it.date == selectedDateLong }?.itineraryPlaceList ?: emptyList()
        } else {
            emptyList()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MapboxMap(
            Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            scaleBar = {},
            logo = {},
            attribution = {},
            style = {
                MapStyle(style = "mapbox://styles/mapbox/satellite-streets-v12")
            }
        )

        if (places.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = dimensionResource(id = R.dimen.module_24))
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.module_16)),
                    verticalAlignment = Alignment.CenterVertically,
                    state = rememberLazyListState()
                ) {
                    itemsIndexed(places) { index, place ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlaceCard(place) {
                                scope.launch {
                                    CommonNavigationChannel.navigateTo(
                                        NavigationAction.Navigate(
                                            AppNavigationScreen.ItineraryDetailView.createRoute(place.placeId)
                                        )
                                    )
                                }
                            }

                            if (index < places.size - 1) {
                                DottedArrowSeparator()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceCard(
    place: ItineraryPlace,
    onPlaceClick: () -> Unit
) {
    val customColors = LocalCustomColors.current

    Box(
        modifier = Modifier
            .width(280.dp)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.module_20)))
            .background(customColors.primaryBackground)
            .padding(dimensionResource(id = R.dimen.module_12))
            .clickable {
                onPlaceClick()
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ComposeImageView.ImageViewWithUrl(
                imageURI = place.imageUrl,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.module_12))),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.module_12)))

            Column(modifier = Modifier.weight(1f)) {
                ComposeTextView.TextView(
                    text = place.placeName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textColor = customColors.titleTextColor,
                    maxLines = 1
                )


                ComposeTextView.TextView(
                    text = place.description,
                    fontSize = 12.sp,
                    maxLines = 2
                )
            }
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopStart)
                .offset(x = (-4).dp, y = (-4).dp)
                .background(customColors.secondaryBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            ComposeTextView.TextView(
                text = place.activityCount.toString(),
                fontSize = 10.sp,
                textColor = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DottedArrowSeparator() {
    val color = LocalCustomColors.current.primaryBackground
    Row(
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_right_arrow),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
    }
}
