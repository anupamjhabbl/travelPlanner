package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.entity.Itinerary
import com.example.bbltripplanner.screens.userTrip.repositories.ItineraryRepository

class ItineraryUseCase(
    private val itineraryRepository: ItineraryRepository
) {
    suspend fun getItinerary(tripId: String): Itinerary? {
        return itineraryRepository.getItinerary(tripId)
    }
}
