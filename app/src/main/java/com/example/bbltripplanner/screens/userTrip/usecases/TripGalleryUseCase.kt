package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.entity.PhotoUploadStatus
import com.example.bbltripplanner.screens.userTrip.entity.TripGalleryUploadRequest
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import com.example.bbltripplanner.screens.userTrip.entity.TripPhotoEntity
import com.example.bbltripplanner.screens.userTrip.entity.toDomain
import com.example.bbltripplanner.screens.userTrip.repositories.TripGalleryRepository

class TripGalleryUseCase(private val repository: TripGalleryRepository) {
    suspend fun fetchTripPhotos(tripId: String) = repository.fetchTripPhotos(tripId)

    suspend fun savePhotosLocally(tripId: String, request: TripGalleryUploadRequest): List<TripPhoto> {
        val entities = request.files.map { file ->
            TripPhotoEntity(
                localPath = file.localPath,
                uploadStatus = PhotoUploadStatus.PENDING,
                tripId = tripId,
                createdAt = file.createdAt,
                fileName = file.fileName,
                mimeType = file.mimeType,
                fileSize = file.fileSize,
                visibility = request.visibility,
                selectedUserIds = request.selectedUserIds,
                isDownloadable = request.isDownloadable,
                isShareable = request.isShareable
            )
        }
        return repository.savePhotosLocally(entities).map { it.toDomain() }
    }

    suspend fun updatePhotoStatus(photoId: Long, status: PhotoUploadStatus) {
        val photos = repository.getPhotosByIds(listOf(photoId))
        photos.firstOrNull()?.let {
            repository.updatePhoto(it.copy(uploadStatus = status))
        }
    }

    suspend fun deletePhoto(photoId: Long) {
        repository.deletePhoto(photoId)
    }
}
