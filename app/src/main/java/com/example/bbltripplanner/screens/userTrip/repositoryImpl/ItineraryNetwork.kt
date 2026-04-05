package com.example.bbltripplanner.screens.userTrip.repositoryImpl

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.clients.ItineraryClient
import com.example.bbltripplanner.screens.userTrip.entity.Itinerary
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryPlaceDetail
import com.example.bbltripplanner.screens.userTrip.repositories.ItineraryRepository

class ItineraryNetwork(
    private val itineraryClient: ItineraryClient
) : ItineraryRepository {
    override suspend fun getItinerary(tripId: String): Itinerary? {
        return BaseResponse.processResponse { itineraryClient.getItinerary(tripId) }
    }

    override suspend fun getItineraryPlaceDetail(placeId: String): ItineraryPlaceDetail? {
        return BaseResponse.processResponse { itineraryClient.getItineraryPlaceDetail(placeId) }
    }
}
