package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.screens.userTrip.entity.TripData

interface UserTripDetailRepository {
    suspend fun getUserTripDetail(tripId: String): TripData
}