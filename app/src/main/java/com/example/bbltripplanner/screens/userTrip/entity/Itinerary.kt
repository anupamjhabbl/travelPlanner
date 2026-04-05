package com.example.bbltripplanner.screens.userTrip.entity

data class Itinerary(
    val itineraryName: String,
    val itinerarySummary: String,
    val itineraryDayList: List<ItineraryDay>
)

data class ItineraryDay(
    val date: Long,
    val spotCount: Int,
    val imageUrl: String,
    val itineraryPlaceList: List<ItineraryPlace> = emptyList()
)

data class ItineraryPlace(
    val placeId: String,
    val placeName: String,
    val locationId: String,
    val description: String,
    val imageUrl: String,
    val activityCount: Int
)
