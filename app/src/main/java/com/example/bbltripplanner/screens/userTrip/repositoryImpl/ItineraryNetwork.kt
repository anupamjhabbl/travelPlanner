package com.example.bbltripplanner.screens.userTrip.repositoryImpl

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.clients.ItineraryClient
import com.example.bbltripplanner.screens.userTrip.entity.*
import com.example.bbltripplanner.screens.userTrip.repositories.ItineraryRepository

class ItineraryNetwork(
    private val itineraryClient: ItineraryClient
) : ItineraryRepository {

    override suspend fun generateItinerary(tripId: String): Itinerary? {
        return BaseResponse.processResponse { itineraryClient.generateItinerary(tripId) }
    }

    override suspend fun getItinerary(tripId: String): Itinerary? {
        return BaseResponse.processResponse { itineraryClient.getItinerary(tripId) }
    }

    override suspend fun addSpot(itineraryId: String, request: AddSpotRequest): ItineraryPlace? {
        val response = BaseResponse.processResponse { itineraryClient.addSpot(itineraryId, request) }
        return response
    }

    override suspend fun getSpots(itineraryId: String): List<ItineraryPlace> {
        return BaseResponse.processResponse { itineraryClient.getSpots(itineraryId) } ?: emptyList()
    }

    override suspend fun addActivity(spotId: String, request: AddActivityRequest): ItineraryActivity? {
        return BaseResponse.processResponse { itineraryClient.addActivity(spotId, request) }
    }

    override suspend fun getActivities(spotId: String): ItineraryActivityResponse? {
        return BaseResponse.processResponse { itineraryClient.getActivities(spotId) }
    }

    override suspend fun updateActivity(activityId: String, request: AddActivityRequest): ItineraryActivity? {
        return BaseResponse.processResponse { itineraryClient.updateActivity(activityId, request) }
    }

    override suspend fun deleteActivity(activityId: String): Boolean {
        BaseResponse.processResponse { itineraryClient.deleteActivity(activityId) }
        return true
    }
}
