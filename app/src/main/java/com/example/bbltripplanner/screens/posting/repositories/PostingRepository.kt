package com.example.bbltripplanner.screens.posting.repositories

import com.example.bbltripplanner.screens.posting.entity.TripData

interface PostingRepository {
    suspend fun postTrip(tripData: TripData): TripData
}