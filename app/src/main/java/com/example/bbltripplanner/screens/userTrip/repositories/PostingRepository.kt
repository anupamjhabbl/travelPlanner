package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.screens.userTrip.entity.TripCreationResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData

interface PostingRepository {
    suspend fun postTrip(tripData: TripData): TripCreationResponse
}