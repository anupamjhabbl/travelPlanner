package com.example.bbltripplanner.screens.userTrip.viewModels

import com.example.bbltripplanner.screens.userTrip.entity.TripGalleryUploadRequest
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto

sealed class TripGalleryIntent {
    sealed class ViewEvent {
        data class FetchPhotos(val tripId: String) : ViewEvent()
        data class FetchTripDetails(val tripId: String) : ViewEvent()
        data class SavePhotosLocally(val request: TripGalleryUploadRequest) : ViewEvent()
        data class SetSelectedPhotos(val photos: List<TripPhoto>) : ViewEvent()
        data class DeletePhoto(val photo: TripPhoto) : ViewEvent()
        data object ClearSelectedPhotos : ViewEvent()
        data class RetryUpload(val photo: TripPhoto) : ViewEvent()
    }

    sealed interface GalleryViewEffect {
        data class UploadError(val message: String) : GalleryViewEffect
        data object UploadSuccess : GalleryViewEffect
        data object SaveSuccess : GalleryViewEffect
    }
}
