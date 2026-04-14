package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.entity.TripCreationResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.repositories.PostingRepository

class PostingUseCase(
    private val postingRepository: PostingRepository
) {
    suspend fun postTrip(tripData: TripData): TripCreationResponse {
        return postingRepository.postTrip(tripData)
    }

    suspend fun updateTrip(tripId: String, tripData: TripData): Boolean {
        return postingRepository.updateTrip(tripId, tripData)
    }
}