package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.userTrip.entity.AddActivityRequest
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryActivityResponse
import com.example.bbltripplanner.screens.userTrip.usecases.ItineraryUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ItineraryDetailViewModel(
    private val itineraryUseCase: ItineraryUseCase
) : BaseMVIVViewModel<ItineraryDetailIntent.ViewEvent>() {

    private val _activitiesStatus = MutableStateFlow(RequestResponseStatus<ItineraryActivityResponse>())
    val activitiesStatus: StateFlow<RequestResponseStatus<ItineraryActivityResponse>> = _activitiesStatus.asStateFlow()

    private val _actionStatus: MutableStateFlow<RequestResponseStatus<Unit>> = MutableStateFlow(RequestResponseStatus())
    val actionStatus: StateFlow<RequestResponseStatus<Unit>> = _actionStatus.asStateFlow()

    private val _viewEffect: Channel<ItineraryDetailIntent.ViewEffect> = Channel()
    val viewEffect = _viewEffect.receiveAsFlow()

    override fun processEvent(viewEvent: ItineraryDetailIntent.ViewEvent) {
        when (viewEvent) {
            is ItineraryDetailIntent.ViewEvent.FetchActivities -> fetchActivities(viewEvent.spotId)
            is ItineraryDetailIntent.ViewEvent.AddActivity -> addActivity(viewEvent.spotId, viewEvent.request)
            is ItineraryDetailIntent.ViewEvent.UpdateActivity -> updateActivity(viewEvent.activityId, viewEvent.request)
            is ItineraryDetailIntent.ViewEvent.DeleteActivity -> deleteActivity(viewEvent.activityId)
        }
    }

    private fun fetchActivities(spotId: String) {
        _activitiesStatus.value = RequestResponseStatus(isLoading = true)
        viewModelScope.launch {
            val result = SafeIOUtil.safeCall {
                itineraryUseCase.getActivities(spotId)
            }
            result.onSuccess { activities ->
                _activitiesStatus.value = RequestResponseStatus(data = activities)
            }
            result.onFailure { error ->
                _activitiesStatus.value = RequestResponseStatus(error = error.message)
            }
        }
    }

    private fun addActivity(spotId: String, request: AddActivityRequest) {
        _actionStatus.value = RequestResponseStatus(isLoading = true)
        viewModelScope.launch {
            val result = SafeIOUtil.safeCall {
                itineraryUseCase.addActivity(spotId, request)
            }
            result.onSuccess { data ->
                if (data != null) {
                    activitiesStatus.value.data?.itineraryActivities?.let {
                        _activitiesStatus.value = activitiesStatus.value.copy(
                            data = activitiesStatus.value.data?.copy(
                                itineraryActivities = it.toMutableList().plus(data)
                            )
                        )
                    }
                }
                _actionStatus.value = RequestResponseStatus(data = Unit)
            }
            result.onFailure { error ->
                _actionStatus.value = RequestResponseStatus(data = Unit)
                if (error is TripPlannerException) {
                    _viewEffect.send(ItineraryDetailIntent.ViewEffect.ErrorInActivityCreation(error.message ?: ""))
                }
            }
        }
    }

    private fun updateActivity(activityId: String, request: AddActivityRequest) {
        _actionStatus.value = RequestResponseStatus(isLoading = true)
        viewModelScope.launch {
            val result = SafeIOUtil.safeCall {
                itineraryUseCase.updateActivity(activityId, request)
            }
            result.onSuccess { responseData ->
                if (responseData != null) {
                    activitiesStatus.value.data?.let {
                        _activitiesStatus.value = activitiesStatus.value.copy(
                            data = it.copy(
                                itineraryActivities = it.itineraryActivities.map { activity ->
                                    if (activity.activityId == responseData.activityId) {
                                        responseData
                                    } else {
                                        activity
                                    }
                                }
                            )
                        )
                    }
                }
                _actionStatus.value = RequestResponseStatus(data = Unit)
            }
            result.onFailure { error ->
                _actionStatus.value = RequestResponseStatus(data = Unit)
                if (error is TripPlannerException) {
                    _viewEffect.send(ItineraryDetailIntent.ViewEffect.ErrorInActivityCreation(error.message ?: ""))
                }
            }
        }
    }

    private fun deleteActivity(activityId: String) {
        _actionStatus.value = RequestResponseStatus(isLoading = true)
        viewModelScope.launch {
            val result = SafeIOUtil.safeCall {
                itineraryUseCase.deleteActivity(activityId)
            }
            result.onSuccess { _ ->
                activitiesStatus.value.data?.let {
                    _activitiesStatus.value = activitiesStatus.value.copy(
                        data = it.copy(
                            itineraryActivities = it.itineraryActivities.filter { activity ->
                                activity.activityId != activityId
                            }
                        )
                    )
                }
                _actionStatus.value = RequestResponseStatus(data = Unit)
            }
            result.onFailure { error ->
                _actionStatus.value = RequestResponseStatus(data = Unit)
                if (error is TripPlannerException) {
                    _viewEffect.send(ItineraryDetailIntent.ViewEffect.ErrorInActivityCreation(error.message ?: ""))
                }
            }
        }
    }
}
