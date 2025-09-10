package com.example.bbltripplanner.screens.posting.repositories

import com.example.bbltripplanner.screens.posting.entity.TripData

interface UserTripDetailRepository {
    suspend fun getUserTripDetail(tripId: String): TripData
}