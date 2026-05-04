package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.TripMember

interface UserTripDetailRepository {
    suspend fun getUserTripDetail(tripId: String): TripData
    suspend fun acceptInvitation(tripId: String): Boolean
    suspend fun getTripMembers(tripId: String): List<TripMember>
    suspend fun addTripMember(tripId: String, userId: String): Boolean
}