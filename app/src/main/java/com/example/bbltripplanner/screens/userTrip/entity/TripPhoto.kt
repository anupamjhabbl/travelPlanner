package com.example.bbltripplanner.screens.userTrip.entity

import androidx.compose.runtime.Stable

@Stable
data class TripPhoto(
    val id: String,
    val url: String,
    val localPath: String? = null,
    val status: PhotoUploadStatus = PhotoUploadStatus.COMPLETE,
    val errorMessage: String? = null
)
