package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.entity.*
import com.example.bbltripplanner.screens.userTrip.repositories.ItineraryRepository

class ItineraryUseCase(
    private val itineraryRepository: ItineraryRepository
) {
    suspend fun generateItinerary(tripId: String): Itinerary? {
        return itineraryRepository.generateItinerary(tripId)
    }

    suspend fun getItinerary(tripId: String): Itinerary? {
        return itineraryRepository.getItinerary(tripId)
    }

    suspend fun addSpot(itineraryId: String, request: AddSpotRequest): ItineraryPlace? {
        return itineraryRepository.addSpot(itineraryId, request)
    }

    suspend fun getSpots(itineraryId: String): List<ItineraryPlace> {
        return itineraryRepository.getSpots(itineraryId)
    }

    suspend fun addActivity(spotId: String, request: AddActivityRequest): ItineraryActivity? {
        return itineraryRepository.addActivity(spotId, request)
    }

    suspend fun getActivities(spotId: String): ItineraryActivityResponse? {
        return itineraryRepository.getActivities(spotId)
    }

    suspend fun updateActivity(activityId: String, request: AddActivityRequest): ItineraryActivity? {
        return itineraryRepository.updateActivity(activityId, request)
    }

    suspend fun deleteActivity(activityId: String): Boolean {
        return itineraryRepository.deleteActivity(activityId)
    }
}
