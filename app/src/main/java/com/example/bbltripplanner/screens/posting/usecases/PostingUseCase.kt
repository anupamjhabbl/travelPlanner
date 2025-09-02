package com.example.bbltripplanner.screens.posting.usecases

import com.example.bbltripplanner.screens.posting.entity.TripData
import com.example.bbltripplanner.screens.posting.repositories.PostingRepository

class PostingUseCase(
    private val postingRepository: PostingRepository
) {
    suspend fun postTrip(tripData: TripData): TripData {
        return postingRepository.postTrip(tripData)
    }
}