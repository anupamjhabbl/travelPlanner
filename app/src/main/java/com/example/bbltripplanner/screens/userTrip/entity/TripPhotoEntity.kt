package com.example.bbltripplanner.screens.userTrip.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_photos")
data class TripPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val localPath: String,
    val uploadStatus: PhotoUploadStatus,
    val tripId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
    val visibility: String? = null,
    val selectedUserIds: List<String> = emptyList(),
    val isDownloadable: Boolean = true,
    val isShareable: Boolean = true
)

fun TripPhotoEntity.toDomain(): TripPhoto {
    return TripPhoto(
        id = id.toString(),
        status = uploadStatus,
        createdAt = createdAt,
        originalMediaUrl = localPath,
        compressedMediaUrl = localPath,
        visibility = visibility,
        selectedUserIds = selectedUserIds,
        isDownloadable = isDownloadable,
        isShareable = isShareable
    )
}
