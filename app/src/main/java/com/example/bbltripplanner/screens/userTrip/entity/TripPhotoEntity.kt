package com.example.bbltripplanner.screens.userTrip.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_photos")
data class TripPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fileId: String? = null,
    val localPath: String,
    val uploadStatus: PhotoUploadStatus,
    val errorMessage: String? = null,
    val tripId: String
)
