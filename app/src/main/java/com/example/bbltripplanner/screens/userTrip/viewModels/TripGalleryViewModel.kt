package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.TripGalleryUploadRequest
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import com.example.bbltripplanner.screens.userTrip.usecases.TripGalleryUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class TripGalleryViewModel(
    savedStateHandle: SavedStateHandle,
    private val useCase: TripGalleryUseCase
) : BaseMVIVViewModel<TripGalleryIntent.ViewEvent>() {

    val tripId = savedStateHandle.get<String>(Constants.NavigationArgs.TRIP_ID)

    private val _photosStatus = MutableStateFlow<RequestResponseStatus<List<TripPhoto>?>>(RequestResponseStatus())
    val photosStatus: StateFlow<RequestResponseStatus<List<TripPhoto>?>> = _photosStatus

    private val _selectedPhotos = MutableStateFlow<List<TripPhoto>>(emptyList())
    val selectedPhotos: StateFlow<List<TripPhoto>> = _selectedPhotos.asStateFlow()

    val galleryPhotos: StateFlow<List<TripPhoto>> = _photosStatus.map { remoteStatus ->
        remoteStatus.data ?: emptyList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

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
            is TripGalleryIntent.ViewEvent.SavePhotosLocally -> savePhotosLocally(viewEvent.request)
            is TripGalleryIntent.ViewEvent.SetSelectedPhotos -> {
                _selectedPhotos.value = viewEvent.photos
            }
            is TripGalleryIntent.ViewEvent.DeletePhoto -> deletePhoto(viewEvent.photo)
        }
    }

    private fun deletePhoto(photo: TripPhoto) {
        viewModelScope.launch {
            _selectedPhotos.value = _selectedPhotos.value.filter { it.id != photo.id }

            photo.originalMediaUrl?.let { path ->
                try {
                    val file = File(path)
                    if (file.exists()) {
                        file.delete()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fetchPhotos(tripId: String) {
        viewModelScope.launch {
            _photosStatus.value = RequestResponseStatus(isLoading = true)
            SafeIOUtil.safeCall {
                useCase.fetchRemotePhotos(tripId)
            }.onSuccess {
                _photosStatus.value = RequestResponseStatus(data = it)
            }.onFailure {
                val errorMessage = if (it is TripPlannerException) it.message ?: Constants.DEFAULT_ERROR else Constants.DEFAULT_ERROR
                _photosStatus.value = RequestResponseStatus(error = errorMessage)
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

    private fun savePhotosLocally(request: TripGalleryUploadRequest) {
        viewModelScope.launch {
            try {
                tripId?.let { id ->
                    val newPhotos = useCase.savePhotosLocally(id, request)
                    val currentPhotos = _photosStatus.value.data ?: emptyList()
                    val updatedPhotos = (newPhotos + currentPhotos).sortedByDescending { it.createdAt }
                    
                    _photosStatus.value = _photosStatus.value.copy(data = updatedPhotos)
                    _galleryViewEffect.send(TripGalleryIntent.GalleryViewEffect.SaveSuccess)
                }
            } catch (e: Exception) {
                _galleryViewEffect.send(TripGalleryIntent.GalleryViewEffect.UploadError(e.message ?: "Failed to save photos"))
            }
        }
    }
}
