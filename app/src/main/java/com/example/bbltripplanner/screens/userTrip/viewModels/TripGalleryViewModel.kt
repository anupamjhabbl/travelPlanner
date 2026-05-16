package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.screens.userTrip.usecases.TripGalleryUseCase
import kotlinx.coroutines.launch

class TripGalleryViewModel(
    private val tripId: String?,
    private val useCase: TripGalleryUseCase
) : ViewModel() {

    val photos = useCase.getTripPhotos(tripId ?: "")

    fun fetchRemotePhotos() {
        viewModelScope.launch {
            useCase.fetchRemotePhotos(tripId ?: "")
        }
    }

    fun uploadPhoto(path: String) {
        viewModelScope.launch {
            useCase.uploadPhoto(tripId ?: "", path)
        }
    }
}
