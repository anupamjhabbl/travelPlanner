package com.example.bbltripplanner.screens.posting.usecases

import com.example.bbltripplanner.screens.posting.entity.TripData
import com.example.bbltripplanner.screens.posting.repositories.UserTripDetailRepository

class UserTripDetailUseCase(
    private val userTripDetailRepository: UserTripDetailRepository
) {
    suspend fun getUserTripDetail(tripId: String): TripData {
        return userTripDetailRepository.getUserTripDetail(tripId)
    }
}