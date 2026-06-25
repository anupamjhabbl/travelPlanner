package com.example.bbltripplanner.screens.userTrip.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.utils.ErrorUtils
import com.example.bbltripplanner.common.utils.ImageActionUtils
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.PhotoUploadStatus
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.TripGalleryUploadRequest
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import com.example.bbltripplanner.screens.userTrip.usecases.TripGalleryUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.UserTripDetailUseCase
import com.example.bbltripplanner.screens.userTrip.workers.TripGalleryUploadWorker
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
    private val context: Application,
    savedStateHandle: SavedStateHandle,
    private val useCase: TripGalleryUseCase,
    private val userTripDetailUseCase: UserTripDetailUseCase
) : BaseMVIVViewModel<TripGalleryIntent.ViewEvent>() {

    val tripId = savedStateHandle.get<String>(Constants.NavigationArgs.TRIP_ID)

    private val _photosStatus = MutableStateFlow<RequestResponseStatus<List<TripPhoto>?>>(RequestResponseStatus())
    val photosStatus: StateFlow<RequestResponseStatus<List<TripPhoto>?>> = _photosStatus

    private val _tripData = MutableStateFlow<RequestResponseStatus<TripData>>(RequestResponseStatus())
    val tripData: StateFlow<RequestResponseStatus<TripData>> = _tripData

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
            processEvent(TripGalleryIntent.ViewEvent.FetchTripDetails(it))
        }
    }

    override fun processEvent(viewEvent: TripGalleryIntent.ViewEvent) {
        when (viewEvent) {
            is TripGalleryIntent.ViewEvent.FetchPhotos -> fetchPhotos(viewEvent.tripId)
            is TripGalleryIntent.ViewEvent.FetchTripDetails -> fetchTripDetails(viewEvent.tripId)
            is TripGalleryIntent.ViewEvent.SavePhotosLocally -> savePhotosLocally(viewEvent.request)
            is TripGalleryIntent.ViewEvent.SetSelectedPhotos -> {
                _selectedPhotos.value = viewEvent.photos
            }
            is TripGalleryIntent.ViewEvent.DeletePhoto -> deletePhoto(viewEvent.photo)
            is TripGalleryIntent.ViewEvent.ClearSelectedPhotos -> clearSelectedPhotos()
            is TripGalleryIntent.ViewEvent.RetryUpload -> retryUpload(viewEvent.photo)
            is TripGalleryIntent.ViewEvent.DownloadImage -> downloadImage(viewEvent.context, viewEvent.url, viewEvent.fileName)
        }
    }

    private fun downloadImage(context: Context, url: String, fileName: String) {
        viewModelScope.launch {
            ImageActionUtils.downloadImage(context, url, fileName)
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

    private fun clearSelectedPhotos() {
        viewModelScope.launch {
            val photosToDelete = _selectedPhotos.value
            _selectedPhotos.value = emptyList()
            
            photosToDelete.forEach { photo ->
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
    }

    private fun retryUpload(photo: TripPhoto) {
        viewModelScope.launch {
            val photoId = photo.id.toLongOrNull() ?: return@launch
            val path = photo.originalMediaUrl ?: return@launch
            val file = File(path)

            if (!file.exists()) {
                useCase.deletePhoto(photoId)
                val currentPhotos = _photosStatus.value.data ?: emptyList()
                _photosStatus.value = _photosStatus.value.copy(
                    data = currentPhotos.filter { it.id != photo.id }
                )
                return@launch
            }

            useCase.updatePhotoStatus(photoId, PhotoUploadStatus.PENDING)
            val currentPhotos = _photosStatus.value.data ?: emptyList()
            val updatedPhotos = currentPhotos.map {
                if (it.id == photo.id) it.copy(status = PhotoUploadStatus.PENDING) else it
            }
            _photosStatus.value = _photosStatus.value.copy(data = updatedPhotos)

            tripId?.let { id ->
                TripGalleryUploadWorker.enqueue(context, longArrayOf(photoId), id)
            }
        }
    }

    private fun fetchPhotos(tripId: String) {
        viewModelScope.launch {
            _photosStatus.value = RequestResponseStatus(isLoading = true)
            SafeIOUtil.safeCall {
                useCase.fetchTripPhotos(tripId)
            }.onSuccess {
                _photosStatus.value = RequestResponseStatus(data = it)
            }.onFailure {
                val errorMessage = ErrorUtils.toErrorType(it)
                _photosStatus.value = RequestResponseStatus(error = errorMessage)
            }
        }
    }

    private fun fetchTripDetails(tripId: String) {
        viewModelScope.launch {
            _tripData.value = RequestResponseStatus(isLoading = true)
            runCatching {
                userTripDetailUseCase.getUserTripDetail(tripId)
            }.onSuccess {
                _tripData.value = RequestResponseStatus(data = it)
            }.onFailure {
                val errorMessage = ErrorUtils.toErrorType(it)
                _tripData.value = RequestResponseStatus(error = errorMessage)
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
                    TripGalleryUploadWorker.enqueue(
                        context,
                        newPhotos
                            .mapNotNull { it.id.toLongOrNull() }
                            .toLongArray(),
                        id)
                    
                    _galleryViewEffect.send(TripGalleryIntent.GalleryViewEffect.SaveSuccess)
                }
            } catch (e: Exception) {
                _galleryViewEffect.send(TripGalleryIntent.GalleryViewEffect.UploadError(e.message ?: Constants.DEFAULT_ERROR_MESSAGE))
            }
        }
    }
}
