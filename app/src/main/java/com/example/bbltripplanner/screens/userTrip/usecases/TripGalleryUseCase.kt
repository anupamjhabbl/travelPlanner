package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.repositories.TripGalleryRepository

class TripGalleryUseCase(private val repository: TripGalleryRepository) {
    fun getTripPhotos(tripId: String) = repository.getPhotos(tripId)
    
    suspend fun fetchRemotePhotos(tripId: String) = repository.fetchRemotePhotos(tripId)

    suspend fun uploadPhoto(tripId: String, path: String) = repository.uploadPhoto(tripId, path)
}
