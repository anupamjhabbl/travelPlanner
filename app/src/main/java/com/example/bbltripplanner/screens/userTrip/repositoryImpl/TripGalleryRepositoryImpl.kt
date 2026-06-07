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

    override suspend fun fetchTripPhotos(tripId: String): List<TripPhoto>? = coroutineScope {
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

    override suspend fun savePhotosLocally(photos: List<TripPhotoEntity>): List<TripPhotoEntity> {
        val ids = dao.insertPhotos(photos)
        return dao.getPhotosByIds(ids)
    }

    override suspend fun getPhotosByIds(ids: List<Long>): List<TripPhotoEntity> {
        return dao.getPhotosByIds(ids)
    }

    override suspend fun getPhotosByStatus(status: PhotoUploadStatus): List<TripPhotoEntity> {
        return dao.getPhotosByStatus(status)
    }

    override suspend fun updatePhoto(photo: TripPhotoEntity) {
        dao.updatePhoto(photo)
    }

    override suspend fun deletePhoto(id: Long) {
        dao.deletePhoto(id)
    }
}
