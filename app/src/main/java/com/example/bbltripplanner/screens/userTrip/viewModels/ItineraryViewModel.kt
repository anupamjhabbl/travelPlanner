package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.utils.ErrorUtils
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.Itinerary
import com.example.bbltripplanner.screens.userTrip.usecases.ItineraryUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ItineraryViewModel(
    tripId: String?,
    private val itineraryUseCase: ItineraryUseCase
) : BaseMVIVViewModel<ItineraryIntent.ViewEvent>() {

    private val _itineraryStatus = MutableStateFlow(RequestResponseStatus<Itinerary>())
    val itineraryStatus: StateFlow<RequestResponseStatus<Itinerary>> = _itineraryStatus.asStateFlow()

    private val _generateItineraryStatus = Channel<ItineraryIntent.GenerateItineraryViewEffect>()
    val generateItineraryStatus: Flow<ItineraryIntent.GenerateItineraryViewEffect> = _generateItineraryStatus.receiveAsFlow()

    init {
        tripId?.let {
            processEvent(ItineraryIntent.ViewEvent.FetchItinerary(it))
        }
    }

    override fun processEvent(viewEvent: ItineraryIntent.ViewEvent) {
        when (viewEvent) {
            is ItineraryIntent.ViewEvent.FetchItinerary -> fetchItinerary(viewEvent.tripId)
            is ItineraryIntent.ViewEvent.GenerateItinerary -> generateItinerary(viewEvent.tripId)
        }
    }

    private fun fetchItinerary(tripId: String) {
        _itineraryStatus.value = RequestResponseStatus(isLoading = true)
        viewModelScope.launch {
            val result = SafeIOUtil.safeCall {
                itineraryUseCase.getItinerary(tripId)
            }
            result.onSuccess { itinerary ->
                _itineraryStatus.value = RequestResponseStatus(data = itinerary)
            }
            result.onFailure { error ->
                val errorMsg = ErrorUtils.toErrorType(error)
                _itineraryStatus.value = RequestResponseStatus(error = errorMsg)
            }
        }
    }

    private fun generateItinerary(tripId: String) {
        viewModelScope.launch {
            _itineraryStatus.value = RequestResponseStatus(isLoading = true)
            _generateItineraryStatus.send(ItineraryIntent.GenerateItineraryViewEffect.GenerateItineraryLoading)
            val result = SafeIOUtil.safeCall {
                itineraryUseCase.generateItinerary(tripId)
            }
            result.onSuccess { itinerary ->
                _itineraryStatus.value = RequestResponseStatus(data = itinerary)
                _generateItineraryStatus.send(ItineraryIntent.GenerateItineraryViewEffect.GenerateItinerarySuccess)
            }
            result.onFailure { error ->
                val errorMsg = ErrorUtils.toErrorType(error)
                _itineraryStatus.value = RequestResponseStatus(isLoading = false)
                _generateItineraryStatus.send(ItineraryIntent.GenerateItineraryViewEffect.GenerateItineraryError(errorMsg))
            }
        }
    }
}
