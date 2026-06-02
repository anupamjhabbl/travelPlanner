package com.example.bbltripplanner.screens.userTrip.entity

import androidx.compose.runtime.Stable

@Stable
data class TripPhoto(
    val id: String,
    val uploadedBy: UploadedBy? = null,
    val status: PhotoUploadStatus = PhotoUploadStatus.COMPLETE,
    val originalMediaUrl: String? = null,
    val compressedMediaUrl: String? = null,
    val createdAt: Long? = null
)

@Stable
data class UploadedBy(
    val id: String,
    val name: String,
    val profilePicture: String?
)
