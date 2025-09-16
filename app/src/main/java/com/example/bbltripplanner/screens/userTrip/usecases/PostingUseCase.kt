package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.repositories.PostingRepository

class PostingUseCase(
    private val postingRepository: PostingRepository
) {
    suspend fun postTrip(tripData: TripData): TripData {
        return postingRepository.postTrip(tripData)
    }
}