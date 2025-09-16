package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.repositories.UserTripDetailRepository

class UserTripDetailUseCase(
    private val userTripDetailRepository: UserTripDetailRepository
) {
    suspend fun getUserTripDetail(tripId: String): TripData {
        return userTripDetailRepository.getUserTripDetail(tripId)
    }
}