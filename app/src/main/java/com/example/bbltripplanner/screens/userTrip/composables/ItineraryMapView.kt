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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.composables.ComposeViewUtils.NewSpotButton
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryPlace
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxDelicateApi
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.style.BooleanValue
import com.mapbox.maps.extension.compose.style.ColorValue
import com.mapbox.maps.extension.compose.style.DoubleListValue
import com.mapbox.maps.extension.compose.style.DoubleValue
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.compose.style.layers.generated.LineCapValue
import com.mapbox.maps.extension.compose.style.layers.generated.LineJoinValue
import com.mapbox.maps.extension.compose.style.layers.generated.LineLayer
import com.mapbox.maps.extension.compose.style.sources.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(MapboxDelicateApi::class)
@Composable
fun ItineraryMapView(
    viewModel: ItineraryViewModel,
    tripSelectedDate: String? = null
) {
    val itineraryStatus by viewModel.itineraryStatus.collectAsState()
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    val accessToken = stringResource(id = R.string.mapbox_access_token)
    var addSpotsDialogVisibility by remember { mutableStateOf(false) }

    val places = remember(itineraryStatus.data, tripSelectedDate) {
        val selectedDateLong = tripSelectedDate?.toLongOrNull()
        if (selectedDateLong != null) {
            itineraryStatus.data?.itineraryDayList?.find { it.date == selectedDateLong }?.itineraryPlaceList ?: emptyList()
        } else {
            emptyList()
        }
    }

    LaunchedEffect(places) {
        if (places.isEmpty() && itineraryStatus.data != null) {
            addSpotsDialogVisibility = true
        }
    }

    if (addSpotsDialogVisibility) {
        ComposeViewUtils.ConfirmationDialog(
            title = stringResource(R.string.add_spots_title),
            message = stringResource(R.string.add_spots_message),
            confirmButtonText = stringResource(R.string.add),
            onConfirm = {
                addSpotsDialogVisibility = false
                scope.launch {
                    CommonNavigationChannel.navigateTo(
                        NavigationAction.Navigate(AppNavigationScreen.SearchScreen.route)
                    )
                }
            },
            onDismiss = { addSpotsDialogVisibility = false }
        )
    }

    val points = remember(places) {
        places.filter { it.location.lat != null && it.location.lon != null }
            .map { Point.fromLngLat(it.location.lon?.toDouble() ?: 0.0, it.location.lat?.toDouble() ?: 0.0) }
    }

    val firstLocation = points.firstOrNull()
    val initialPoint = remember(firstLocation) {
        firstLocation ?: Point.fromLngLat(0.0, 0.0)
    }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(12.0)
            center(initialPoint)
            pitch(0.0)
            bearing(0.0)
        }
    }

    val routeSourceState = rememberGeoJsonSourceState {
        lineMetrics = BooleanValue(true)
    }

    LaunchedEffect(points) {
        if (points.size > 1) {
            val routeOptions = RouteOptions.builder()
                .baseUrl(Constants.BASE_API_URL)
                .user(Constants.MAPBOX_USER)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .coordinatesList(points)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .build()

            val builder = MapboxDirections.builder()
                .routeOptions(routeOptions)
                .accessToken(accessToken)

            builder.build().enqueueCall(object : Callback<DirectionsResponse> {
                override fun onResponse(
                    call: Call<DirectionsResponse>,
                    response: Response<DirectionsResponse>
                ) {
                    val routes = response.body()?.routes()
                    if (!routes.isNullOrEmpty()) {
                        val routeGeometry = routes[0].geometry()
                        if (routeGeometry != null) {
                            routeSourceState.data = GeoJSONData(LineString.fromPolyline(routeGeometry, 6))
                        }
                    }
                }

                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {}
            })
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MapboxMap(
            Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            scaleBar = {},
            logo = {},
            attribution = {},
            compass = {},
            style = {
                MapStyle(style = "mapbox://styles/mapbox/satellite-streets-v12")
            }
        ) {
            LineLayer(
                sourceState = routeSourceState,
                layerId = "route-layer"
            ) {
                lineColor = ColorValue(customColors.secondaryBackground)
                lineWidth = DoubleValue(4.0)
                lineDasharray = DoubleListValue(listOf(2.0, 2.0))
                lineCap = LineCapValue.ROUND
                lineJoin = LineJoinValue.ROUND
            }

            places.forEachIndexed { index, place ->
                if (place.location.lat != null && place.location.lon != null) {
                    val point = Point.fromLngLat(place.location.lon.toDouble(), place.location.lat.toDouble())
                    ViewAnnotation(
                        options = viewAnnotationOptions {
                            geometry(point)
                        }
                    ) {
                        ViewAnnotationContent(index) {
                            mapViewportState.flyTo(
                                cameraOptions {
                                    center(point)
                                    zoom(15.0)
                                }
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 60.dp, end = 24.dp)
        ) {
            Column(horizontalAlignment = Alignment.End) {
                NewSpotButton {
                    addSpotsDialogVisibility = false
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(AppNavigationScreen.SearchScreen.route)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (places.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_right_arrow),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(32.dp)
                                .offset(y = (-8).dp, x = (-8).dp)
                        )
                        ComposeTextView.TextView(
                            text = stringResource(id = R.string.tap_to_add_new_spot),
                            fontSize = 14.sp,
                            textColor = Color.White,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
        }

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

@Composable
fun ViewAnnotationContent(
    placeIndex: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(30.dp)
            .clip(CircleShape)
            .background(LocalCustomColors.current.secondaryBackground)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        ComposeTextView.TextView(
            text = (placeIndex + 1).toString(),
            fontSize = 16.sp,
            textColor = Color.White
        )
    }
}
