package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.BuildConfig
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.AddSpotRequest
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryPlace
import com.example.bbltripplanner.screens.userTrip.usecases.ItineraryUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.LocationSearchUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ItineraryMapViewModel(
    itineraryId: String?,
    private val itineraryUseCase: ItineraryUseCase,
    private val locationSearchUseCase: LocationSearchUseCase
) : BaseMVIVViewModel<ItineraryMapIntent.ViewEvent>() {
    private val _spotsStatus = MutableStateFlow(RequestResponseStatus<List<ItineraryPlace>>())
    val spotsStatus: StateFlow<RequestResponseStatus<List<ItineraryPlace>>> = _spotsStatus.asStateFlow()

    private val _actionStatus = MutableStateFlow<RequestResponseStatus<Unit>>(RequestResponseStatus())
    val actionStatus: StateFlow<RequestResponseStatus<Unit>> = _actionStatus.asStateFlow()

    private val _viewEffect: Channel<ItineraryMapIntent.ViewEffect> = Channel()
    val viewEffect = _viewEffect.receiveAsFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        itineraryId?.let {
            processEvent(ItineraryMapIntent.ViewEvent.FetchSpots(it))
        }

        viewModelScope.launch {
            searchQuery
                .debounce(300L)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collect {
                    getLocationSuggestions(it)
                }
        }
    }

    override fun processEvent(viewEvent: ItineraryMapIntent.ViewEvent) {
        when (viewEvent) {
            is ItineraryMapIntent.ViewEvent.AddSpot -> addSpot(viewEvent.itineraryId, viewEvent.request)
            is ItineraryMapIntent.ViewEvent.FetchSpots -> fetchSpots(viewEvent.itineraryId)
            is ItineraryMapIntent.ViewEvent.OnQueryChanged -> onQueryChanged(viewEvent.query)
        }
    }

    private fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun getLocationSuggestions(query: String) {
        viewModelScope.launch {
            _viewEffect.send(ItineraryMapIntent.ViewEffect.ShowLocationLoading)
            val result = SafeIOUtil.safeCall {
                locationSearchUseCase.getLocationSuggestions(BuildConfig.LOCATION_API_KEY, query)
            }
            result.onSuccess { suggestions ->
                _viewEffect.send(ItineraryMapIntent.ViewEffect.HideLocationLoading)
                _viewEffect.send(ItineraryMapIntent.ViewEffect.ShowSuggestions(suggestions))
            }
            result.onFailure {
                _viewEffect.send(ItineraryMapIntent.ViewEffect.HideLocationLoading)
                _viewEffect.send(ItineraryMapIntent.ViewEffect.ShowSuggestions(emptyList()))
            }
        }
    }

    private fun addSpot(itineraryId: String, request: AddSpotRequest) {
        _actionStatus.value = RequestResponseStatus(isLoading = true)
        viewModelScope.launch {
            val result = SafeIOUtil.safeCall {
                itineraryUseCase.addSpot(itineraryId, request)
            }
            result.onSuccess { data ->
                if (data != null) {
                    _spotsStatus.value = spotsStatus.value.copy(
                        data = (spotsStatus.value.data ?: emptyList()).plus(data)
                    )
                }
                _actionStatus.value = RequestResponseStatus(data = Unit)
            }
            result.onFailure { error ->
                _actionStatus.value = RequestResponseStatus(data = Unit)
                if (error is TripPlannerException) {
                    _viewEffect.send(ItineraryMapIntent.ViewEffect.ErrorInSpotCreation(error.message ?: ""))
                }
            }
        }
    }

    private fun fetchSpots(itineraryId: String) {
        _spotsStatus.value = RequestResponseStatus(isLoading = true)
        viewModelScope.launch {
            val result = SafeIOUtil.safeCall {
                itineraryUseCase.getSpots(itineraryId)
            }
            result.onSuccess { spots ->
                _spotsStatus.value = RequestResponseStatus(data = spots)
            }
            result.onFailure { error ->
                _spotsStatus.value = RequestResponseStatus(error = error.message)
            }
        }
    }
}
