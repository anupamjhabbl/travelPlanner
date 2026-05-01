package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.screens.userTrip.entity.*

interface ItineraryRepository {
    suspend fun generateItinerary(tripId: String): Itinerary?
    suspend fun getItinerary(tripId: String): Itinerary?
    suspend fun addSpot(itineraryId: String, request: AddSpotRequest): ItineraryPlace?
    suspend fun getSpots(itineraryId: String): List<ItineraryPlace>
    suspend fun addActivity(spotId: String, request: AddActivityRequest): ItineraryActivity?
    suspend fun getActivities(spotId: String): ItineraryActivityResponse?
    suspend fun updateActivity(activityId: String, request: AddActivityRequest): ItineraryActivity?
    suspend fun deleteActivity(activityId: String): Boolean
}
