package com.example.bbltripplanner.screens.userTrip.entity

data class TripPhotoResponse(
    val id: String,
    val uploadedBy: UploadedBy?,
    val fileName: String?,
    val mediaType: String?,
    val fileSize: Long?,
    val originalUrl: String?,
    val compressedUrl: String?,
    val createdAt: Long?
)

fun TripPhotoResponse.toDomain(): TripPhoto {
    return TripPhoto(
        id = id,
        uploadedBy = uploadedBy,
        status = PhotoUploadStatus.COMPLETE,
        originalMediaUrl = originalUrl,
        compressedMediaUrl = compressedUrl,
        createdAt = createdAt
    )
}
