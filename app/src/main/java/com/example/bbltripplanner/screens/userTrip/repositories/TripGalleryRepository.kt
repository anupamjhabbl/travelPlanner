package com.example.bbltripplanner.screens.userTrip.repositories

import com.example.bbltripplanner.screens.userTrip.entity.PhotoUploadStatus
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import com.example.bbltripplanner.screens.userTrip.entity.TripPhotoEntity
import kotlinx.coroutines.flow.Flow

interface TripGalleryRepository {
    fun getPhotos(tripId: String): Flow<List<TripPhoto>>
    suspend fun fetchTripPhotos(tripId: String): List<TripPhoto>?
    suspend fun savePhotosLocally(photos: List<TripPhotoEntity>): List<TripPhotoEntity>
    suspend fun getPhotosByIds(ids: List<Long>): List<TripPhotoEntity>
    suspend fun getPhotosByStatus(status: PhotoUploadStatus): List<TripPhotoEntity>
    suspend fun updatePhoto(photo: TripPhotoEntity)
    suspend fun deletePhoto(id: Long)
}
