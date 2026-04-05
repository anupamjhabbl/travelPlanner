package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.screens.userTrip.entity.Itinerary
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryPlaceDetail

interface ItineraryRepository {
    suspend fun getItinerary(tripId: String): Itinerary?
    suspend fun getItineraryPlaceDetail(placeId: String): ItineraryPlaceDetail?
}
