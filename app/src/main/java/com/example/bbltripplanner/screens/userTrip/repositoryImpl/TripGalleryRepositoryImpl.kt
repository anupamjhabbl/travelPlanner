package com.example.bbltripplanner.screens.userTrip.repositoryImpl

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.clients.TripGalleryClient
import com.example.bbltripplanner.screens.userTrip.dao.TripPhotoDao
import com.example.bbltripplanner.screens.userTrip.entity.PhotoUploadStatus
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import com.example.bbltripplanner.screens.userTrip.entity.TripPhotoEntity
import com.example.bbltripplanner.screens.userTrip.entity.toDomain
import com.example.bbltripplanner.screens.userTrip.repositories.TripGalleryRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripGalleryRepositoryImpl(
    private val client: TripGalleryClient,
    private val dao: TripPhotoDao
) : TripGalleryRepository {

    override fun getPhotos(tripId: String): Flow<List<TripPhoto>> {
        return dao.getPhotosForTrip(tripId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun fetchRemotePhotos(tripId: String): List<TripPhoto>? = coroutineScope {
        val remoteDeferred = async {
            try {
                BaseResponse.processResponse { client.getPhotos(tripId) }
            } catch (_: Exception) {
                null
            }
        }
        val localIncompleteDeferred = async {
            dao.getIncompletePhotos(tripId).map { it.toDomain() }
        }

        val remotePhotos = remoteDeferred.await()?.map { it.toDomain() }
        val localIncompletePhotos = localIncompleteDeferred.await()

        if (remotePhotos == null && localIncompletePhotos.isEmpty()) {
            return@coroutineScope null
        }

        val combined = (remotePhotos ?: emptyList()) + localIncompletePhotos
        combined.sortedByDescending { it.createdAt }
    }

    override suspend fun uploadPhoto(tripId: String, localPath: String) {
        val entity = TripPhotoEntity(
            localPath = localPath,
            uploadStatus = PhotoUploadStatus.PENDING,
            tripId = tripId,
            createdAt = System.currentTimeMillis()
        )
        dao.insertPhoto(entity)
    }

    override suspend fun savePhotosLocally(photos: List<TripPhotoEntity>): List<TripPhoto> {
        val ids = dao.insertPhotos(photos)
        return dao.getPhotosByIds(ids).map { it.toDomain() }
    }
}
