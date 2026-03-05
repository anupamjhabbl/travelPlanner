package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData

interface UserTripDetailRepository {
    suspend fun getUserTripDetail(tripId: String): TripData
    suspend fun acceptInvitation(tripId: String): Boolean
}