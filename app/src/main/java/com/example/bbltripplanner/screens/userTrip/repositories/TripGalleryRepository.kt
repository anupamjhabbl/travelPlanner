package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import kotlinx.coroutines.flow.Flow

interface TripGalleryRepository {
    fun getPhotos(tripId: String): Flow<List<TripPhoto>>
    suspend fun fetchRemotePhotos(tripId: String): List<TripPhoto>?
    suspend fun uploadPhoto(tripId: String, localPath: String)
}
