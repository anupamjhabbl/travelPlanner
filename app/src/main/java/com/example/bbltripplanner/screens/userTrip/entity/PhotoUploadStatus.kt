package com.example.bbltripplanner.screens.userTrip.entity

import com.google.gson.annotations.SerializedName

enum class PhotoUploadStatus {
    @SerializedName("PENDING")
    PENDING,
    @SerializedName("UPLOADING")
    UPLOADING,
    @SerializedName("FAILED")
    FAILED,
    @SerializedName("PROCESSED")
    COMPLETE
}
