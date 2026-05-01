package com.example.bbltripplanner.screens.userTrip.entity

data class Itinerary(
    val itineraryName: String,
    val itinerarySummary: String? = null,
    val itineraryList: List<ItineraryDay>
)

data class ItineraryDay(
    val itineraryId: String,
    val date: Long,
    val imageUrl: String? = null,
)

data class ItineraryPlaceResponse(
    val itineraryPlace: ItineraryPlace
)

data class ItineraryPlace(
    val placeId: String,
    val placeName: String,
    val location: Location,
    val description: String? = null,
    val imageUrl: String? = null,
    val activityCount: Int? = 0
)
