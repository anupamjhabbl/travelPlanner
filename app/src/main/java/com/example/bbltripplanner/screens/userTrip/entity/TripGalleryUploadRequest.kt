package com.example.bbltripplanner.screens.userTrip.entity

data class TripGalleryUploadRequest(
    val visibility: AttachmentPrivacy,
    val selectedUserIds: List<String> = emptyList(),
    val isDownloadable: Boolean = true,
    val isShareable: Boolean = true,
    val files: List<TripGalleryFile>
)

data class TripGalleryFile(
    val fileName: String,
    val mimeType: String,
    val fileSize: Long,
    val createdAt: Long,
    val localPath: String
)

data class PresignedUrlRequest(
    val visibility: AttachmentPrivacy,
    val selectedUserIds: List<String> = emptyList(),
    val files: List<PresignedUrlFile>
)

data class PresignedUrlFile(
    val fileName: String,
    val mimeType: String,
    val fileSize: Long,
    val createdAt: Long
)

data class PresignedUrlResponse(
    val mediaId: String,
    val uploadUrl: String
)
