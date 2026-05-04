package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.TripMember
import com.example.bbltripplanner.screens.userTrip.repositories.UserTripDetailRepository

class UserTripDetailUseCase(
    private val userTripDetailRepository: UserTripDetailRepository
) {
    suspend fun getUserTripDetail(tripId: String): TripData {
        return userTripDetailRepository.getUserTripDetail(tripId)
    }

    suspend fun acceptInvitation(tripId: String): Boolean {
        return userTripDetailRepository.acceptInvitation(tripId)
    }

    suspend fun getTripMembers(tripId: String): List<TripMember> {
        return userTripDetailRepository.getTripMembers(tripId)
    }

    suspend fun addTripMember(tripId: String, userId: String): Boolean {
        return userTripDetailRepository.addTripMember(tripId, userId)
    }
}