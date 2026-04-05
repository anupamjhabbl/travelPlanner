package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryPlaceDetail
import com.example.bbltripplanner.screens.userTrip.usecases.ItineraryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItineraryDetailViewModel(
    private val itineraryUseCase: ItineraryUseCase
) : BaseMVIVViewModel<ItineraryDetailIntent.ViewEvent>() {

    private val _placeDetailStatus = MutableStateFlow(RequestResponseStatus<ItineraryPlaceDetail>())
    val placeDetailStatus: StateFlow<RequestResponseStatus<ItineraryPlaceDetail>> = _placeDetailStatus.asStateFlow()

    override fun processEvent(viewEvent: ItineraryDetailIntent.ViewEvent) {
        when (viewEvent) {
            is ItineraryDetailIntent.ViewEvent.FetchPlaceDetail -> fetchPlaceDetail(viewEvent.placeId)
        }
    }

    private fun fetchPlaceDetail(placeId: String) {
        _placeDetailStatus.value = RequestResponseStatus(isLoading = true)
        viewModelScope.launch {
            val result = SafeIOUtil.safeCall {
                itineraryUseCase.getItineraryPlaceDetail(placeId)
            }
            result.onSuccess { detail ->
                _placeDetailStatus.value = RequestResponseStatus(data = detail)
            }
            result.onFailure { error ->
                _placeDetailStatus.value = RequestResponseStatus(error = error.message)
            }
        }
    }
}
