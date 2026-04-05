package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.screens.userTrip.entity.Itinerary

interface ItineraryRepository {
    suspend fun getItinerary(tripId: String): Itinerary?
}
