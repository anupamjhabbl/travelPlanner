package com.example.bbltripplanner.screens.userTrip.entity

data class ItineraryPlaceDetail(
    val placeId: String,
    val placeName: String,
    val imageUrl: String,
    val description: String,
    val activityList: List<ItineraryActivity> = emptyList(),
    val rating: Double? = 4.8,
    val address: String? = null
)

data class ItineraryActivity(
    val activityName: String,
    val description: String,
    val startTime: String,
    val endTime: String
)
