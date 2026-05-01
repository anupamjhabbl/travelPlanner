package com.example.bbltripplanner.screens.userTrip.entity

data class ItineraryActivity(
    val activityId: String? = null,
    val activityName: String,
    val description: String? = null,
    val startTime: Long,
    val endTime: Long
)

data class AddSpotRequest(
    val placeName: String,
    val location: LocationRequestModel,
    val description: String
)

data class AddActivityRequest(
    val activityName: String,
    val description: String,
    val startTime: Long,
    val endTime: Long
)

data class ItineraryActivityResponse(
    val placeName: String,
    val location: Location? = null,
    val description: String? = null,
    val date: Long,
    val itineraryActivities: List<ItineraryActivity> = emptyList()
)
