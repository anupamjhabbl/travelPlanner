package com.example.bbltripplanner.screens.userTrip.repositoryImpl

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.clients.TripGalleryClient
import com.example.bbltripplanner.screens.userTrip.dao.TripPhotoDao
import com.example.bbltripplanner.screens.userTrip.entity.PhotoUploadStatus
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import com.example.bbltripplanner.screens.userTrip.entity.TripPhotoEntity
import com.example.bbltripplanner.screens.userTrip.repositories.TripGalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripGalleryRepositoryImpl(
    private val client: TripGalleryClient,
    private val dao: TripPhotoDao
) : TripGalleryRepository {

    override fun getPhotos(tripId: String): Flow<List<TripPhoto>> {
        return dao.getPhotosForTrip(tripId).map { entities ->
            entities.map { entity ->
                TripPhoto(
                    id = entity.id.toString(),
                    url = "", // Local photos might not have a URL yet
                    localPath = entity.localPath,
                    status = entity.uploadStatus,
                    errorMessage = entity.errorMessage
                )
            }
        }
    }

    override suspend fun fetchRemotePhotos(tripId: String): List<TripPhoto>? {
        return BaseResponse.processResponse { client.getPhotos(tripId) }
    }

    override suspend fun uploadPhoto(tripId: String, localPath: String) {
        val entity = TripPhotoEntity(
            localPath = localPath,
            uploadStatus = PhotoUploadStatus.PENDING,
            tripId = tripId
        )
        dao.insertPhoto(entity)
        // Trigger WorkManager logic would be in the UseCase or handled by an observer
    }
}
