package com.example.bbltripplanner.screens.userTrip.entity

data class Itinerary(
    val itineraryName: String,
    val itinerarySummary: String,
    val itineraryDayList: List<ItineraryDay>
)

data class ItineraryDay(
    val date: Long,
    val spotCount: Int,
    val imageUrl: String
)
