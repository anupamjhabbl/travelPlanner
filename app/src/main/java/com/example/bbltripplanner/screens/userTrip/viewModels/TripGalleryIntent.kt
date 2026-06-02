package com.example.bbltripplanner.screens.userTrip.viewModels

sealed class TripGalleryIntent {
    sealed class ViewEvent {
        data class FetchPhotos(val tripId: String) : ViewEvent()
        data class UploadPhoto(val tripId: String, val path: String) : ViewEvent()
    }

    sealed interface GalleryViewEffect {
        data object UploadLoading : GalleryViewEffect
        data class UploadError(val message: String) : GalleryViewEffect
        data object UploadSuccess : GalleryViewEffect
    }
}
