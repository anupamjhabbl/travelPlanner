package com.example.bbltripplanner.screens.userTrip.usecases

import com.example.bbltripplanner.screens.userTrip.entity.PhotoUploadStatus
import com.example.bbltripplanner.screens.userTrip.entity.TripGalleryUploadRequest
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import com.example.bbltripplanner.screens.userTrip.entity.TripPhotoEntity
import com.example.bbltripplanner.screens.userTrip.repositories.TripGalleryRepository

class TripGalleryUseCase(private val repository: TripGalleryRepository) {
    suspend fun fetchRemotePhotos(tripId: String) = repository.fetchRemotePhotos(tripId)

    suspend fun uploadPhoto(tripId: String, path: String) = repository.uploadPhoto(tripId, path)

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
        return repository.savePhotosLocally(entities)
    }
}
