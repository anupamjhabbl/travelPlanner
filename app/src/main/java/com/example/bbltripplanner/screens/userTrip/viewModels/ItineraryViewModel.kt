package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.Itinerary
import com.example.bbltripplanner.screens.userTrip.usecases.ItineraryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItineraryViewModel(
    private val itineraryUseCase: ItineraryUseCase
) : BaseMVIVViewModel<ItineraryIntent.ViewEvent>() {

    private val _itineraryStatus = MutableStateFlow(RequestResponseStatus<Itinerary>())
    val itineraryStatus: StateFlow<RequestResponseStatus<Itinerary>> = _itineraryStatus.asStateFlow()

    override fun processEvent(viewEvent: ItineraryIntent.ViewEvent) {
        when (viewEvent) {
            is ItineraryIntent.ViewEvent.FetchItinerary -> fetchItinerary(viewEvent.tripId)
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
                _itineraryStatus.value = RequestResponseStatus(error = error.message)
            }
        }
    }
}
