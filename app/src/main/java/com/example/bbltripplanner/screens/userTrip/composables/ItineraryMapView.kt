package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle

@Composable
fun ItineraryMapView(
    tripId: String?,
    tripSelectedDate: String? = null
) {
    MapboxMap(
        Modifier.fillMaxSize(),
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(9.0)
                center(Point.fromLngLat(77.0958, 28.6573))
                pitch(0.0)
                bearing(0.0)
            }
        },
        scaleBar = {},
        logo = {},
        attribution = {},
        style = {
            MapStyle(style = "mapbox://styles/mapbox/satellite-streets-v12")
        }
    )
}