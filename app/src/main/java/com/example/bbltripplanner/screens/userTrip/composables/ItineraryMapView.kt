package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.CommonLifecycleAwareLaunchedEffect
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.composables.ComposeViewUtils.NewSpotButton
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.AddSpotRequest
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryPlace
import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.screens.userTrip.entity.toModel
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryMapIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryMapViewModel
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
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(MapboxDelicateApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ItineraryMapView(
    itineraryId: String? = null,
) {
    val context = LocalContext.current
    val itineraryMapViewModel: ItineraryMapViewModel = koinViewModel(parameters = { parametersOf(itineraryId) })
    val spotsStatus by itineraryMapViewModel.spotsStatus.collectAsState()
    val actionStatus by itineraryMapViewModel.actionStatus.collectAsState()
    var showLocationBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var locationSuggestions by remember { mutableStateOf(emptyList<Location>()) }
    var isLocationLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    val searchQuery by itineraryMapViewModel.searchQuery.collectAsState()
    val accessToken = stringResource(id = R.string.mapbox_access_token)
    
    var addSpotsDialogVisibility by remember { mutableStateOf(false) }
    var showAddSpotForm by remember { mutableStateOf(false) }
    
    var placeName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedLocation: Location? by remember { mutableStateOf(null) }

    LaunchedEffect(spotsStatus) {
        if (!spotsStatus.isLoading && spotsStatus.data.isNullOrEmpty() && spotsStatus.error == null) {
            addSpotsDialogVisibility = true
        }
    }

    CommonLifecycleAwareLaunchedEffect(itineraryMapViewModel.viewEffect) { viewEffect ->
        when (viewEffect) {
            is ItineraryMapIntent.ViewEffect.ErrorInSpotCreation -> {
                ComposeViewUtils.showToast(context, viewEffect.message)
            }
            ItineraryMapIntent.ViewEffect.HideLocationLoading -> {
                isLocationLoading = false
            }
            ItineraryMapIntent.ViewEffect.ShowLocationLoading -> {
                isLocationLoading = true
            }
            is ItineraryMapIntent.ViewEffect.ShowSuggestions -> {
                locationSuggestions = viewEffect.suggestions
            }
        }
    }

    if (addSpotsDialogVisibility) {
        AlertDialog(
            onDismissRequest = { },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            title = { ComposeTextView.TitleTextView(text = stringResource(R.string.add_spots_title)) },
            text = { ComposeTextView.TextView(text = stringResource(R.string.add_spots_message), fontSize = 16.sp) },
            confirmButton = {
                ComposeButtonView.PrimaryButtonView(
                    modifier = Modifier.width(60.dp),
                    text = stringResource(R.string.ok),
                    onClick = {
                        addSpotsDialogVisibility = false
                        showAddSpotForm = true
                    }
                )
            },
            dismissButton = {
                ComposeButtonView.SecondaryButtonView(
                    text = stringResource(R.string.cancel),
                    onClick = { addSpotsDialogVisibility = false }
                )
            },
            containerColor = customColors.primaryBackground
        )
    }

    if (showAddSpotForm) {
        val selectLocationMessage = stringResource(R.string.select_location)
        AlertDialog(
            onDismissRequest = { },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            title = { ComposeTextView.TitleTextView(text = stringResource(R.string.add_new_spot)) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(
                        value = placeName,
                        onValueChange = { placeName = it },
                        label = { ComposeTextView.TextView(text = stringResource(R.string.place_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = customColors.secondaryBackground),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ClickableFieldBox(
                        text = selectedLocation?.displayName ?: "",
                        placeholder = stringResource(R.string.where_to)
                    ) {
                        showLocationBottomSheet = true
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { ComposeTextView.TextView(text = stringResource(R.string.description)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = customColors.secondaryBackground),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                ComposeButtonView.PrimaryButtonView(
                    text = stringResource(R.string.submit),
                    onClick = {
                        if (itineraryId != null) {
                            selectedLocation?.let {
                                itineraryMapViewModel.processEvent(
                                    ItineraryMapIntent.ViewEvent.AddSpot(
                                        itineraryId,
                                        AddSpotRequest(
                                            placeName = placeName,
                                            description = description,
                                            location = it.toModel()
                                        )
                                    )
                                )
                                showAddSpotForm = false
                            } ?: run {
                                ComposeViewUtils.showToast(context, selectLocationMessage)
                            }
                            placeName = ""
                            description = ""
                            selectedLocation = null
                        }
                    }
                )
            },
            dismissButton = {
                ComposeButtonView.SecondaryButtonView(
                    text = stringResource(R.string.cancel),
                    onClick = { showAddSpotForm = false }
                )
            },
            containerColor = customColors.primaryBackground
        )
    }

    if (showLocationBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            onDismissRequest = { showLocationBottomSheet = false },
            sheetState = sheetState,
            containerColor = customColors.primaryBackground
        ) {
            LocationBottomSheet(
                locationList = locationSuggestions,
                searchQuery = searchQuery,
                isLocationLoading = isLocationLoading,
                onQueryChanged = { itineraryMapViewModel.processEvent(ItineraryMapIntent.ViewEvent.OnQueryChanged(it)) },
                updateLocation = {
                    selectedLocation = it
                    showLocationBottomSheet = false
                }
            )
        }
    }

    val places = spotsStatus.data ?: emptyList()

    val points = remember(places) {
        places.mapNotNull { place ->
            val lat = place.location.lat?.toDoubleOrNull()
            val lon = place.location.lon?.toDoubleOrNull()
            if (lat != null && lon != null) {
                Point.fromLngLat(lon, lat)
            } else {
                null
            }
        }
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

    LaunchedEffect(initialPoint) {
        if (initialPoint.latitude() != 0.0 || initialPoint.longitude() != 0.0) {
            mapViewportState.flyTo(
                cameraOptions {
                    center(initialPoint)
                    zoom(12.0)
                }
            )
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
                val lat = place.location.lat?.toDoubleOrNull()
                val lon = place.location.lon?.toDoubleOrNull()
                if (lat != null && lon != null) {
                    val point = Point.fromLngLat(lon, lat)
                    ViewAnnotation(
                        options = viewAnnotationOptions {
                            geometry(point)
                            allowOverlap(true)
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
                    showAddSpotForm = true
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
        
        if (spotsStatus.isLoading || actionStatus.isLoading) {
            ComposeViewUtils.FullScreenLoading()
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
                imageURI = place.imageUrl ?: "",
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
                    text = place.description ?: "",
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
                text = (place.activityCount ?: 0).toString(),
                fontSize = 10.sp,
                textColor = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DottedArrowSeparator() {
    val color = Color.White
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
