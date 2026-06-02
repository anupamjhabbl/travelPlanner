package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import com.example.bbltripplanner.screens.userTrip.usecases.TripGalleryUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TripGalleryViewModel(
    savedStateHandle: SavedStateHandle,
    private val useCase: TripGalleryUseCase
) : BaseMVIVViewModel<TripGalleryIntent.ViewEvent>() {

    val tripId = savedStateHandle.get<String>(Constants.NavigationArgs.TRIP_ID)

    private val _remotePhotosStatus = MutableStateFlow<RequestResponseStatus<List<TripPhoto>?>>(RequestResponseStatus())
    val remotePhotosStatus: StateFlow<RequestResponseStatus<List<TripPhoto>?>> = _remotePhotosStatus

    private val _galleryViewEffect = Channel<TripGalleryIntent.GalleryViewEffect>()
    val galleryViewEffect: Flow<TripGalleryIntent.GalleryViewEffect> = _galleryViewEffect.receiveAsFlow()

    init {
        tripId?.let {
            processEvent(TripGalleryIntent.ViewEvent.FetchPhotos(it))
        }
    }

    override fun processEvent(viewEvent: TripGalleryIntent.ViewEvent) {
        when (viewEvent) {
            is TripGalleryIntent.ViewEvent.FetchPhotos -> fetchPhotos(viewEvent.tripId)
            is TripGalleryIntent.ViewEvent.UploadPhoto -> uploadPhoto(viewEvent.tripId, viewEvent.path)
        }
    }

    private fun fetchPhotos(tripId: String) {
        viewModelScope.launch {
            _remotePhotosStatus.value = RequestResponseStatus(isLoading = true)
            SafeIOUtil.safeCall {
                useCase.fetchRemotePhotos(tripId)
            }.onSuccess {
                _remotePhotosStatus.value = RequestResponseStatus(data = it)
            }.onFailure {
                val errorMessage = if (it is TripPlannerException) it.message ?: Constants.DEFAULT_ERROR else Constants.DEFAULT_ERROR
                _remotePhotosStatus.value = RequestResponseStatus(error = errorMessage)
            }
        }
    }

    private fun uploadPhoto(tripId: String, path: String) {
        viewModelScope.launch {
            _galleryViewEffect.send(TripGalleryIntent.GalleryViewEffect.UploadLoading)
            try {
                useCase.uploadPhoto(tripId, path)
                _galleryViewEffect.send(TripGalleryIntent.GalleryViewEffect.UploadSuccess)
            } catch (e: Exception) {
                _galleryViewEffect.send(TripGalleryIntent.GalleryViewEffect.UploadError(e.message ?: Constants.DEFAULT_ERROR))
            }
        }
    }
}
