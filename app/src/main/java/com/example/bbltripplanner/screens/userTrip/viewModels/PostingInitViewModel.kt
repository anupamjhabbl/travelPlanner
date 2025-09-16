package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.home.entities.Location
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.TripVisibility
import com.example.bbltripplanner.screens.userTrip.usecases.PostingUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostingInitViewModel(
    private val postingUseCase: PostingUseCase
): BaseMVIVViewModel<PostingInitIntent.ViewEvent>() {
    private val _tripFormData: MutableStateFlow<TripData> = MutableStateFlow(TripData())
    val tripFormData: StateFlow<TripData> = _tripFormData.asStateFlow()

    private val _viewEffects: MutableSharedFlow<PostingInitIntent.ViewEffect> = MutableSharedFlow()
    val viewEffect: SharedFlow<PostingInitIntent.ViewEffect> = _viewEffects.asSharedFlow()

    fun updateTripLocation(location: Location) {
        _tripFormData.value = tripFormData.value.copy(tripLocation = location)
    }

    fun updateTripStartDate(startDate: String) {
        _tripFormData.value = tripFormData.value.copy(startDate = startDate)
    }

    fun updateTripEndDate(endDate: String) {
        _tripFormData.value = tripFormData.value.copy(endDate = endDate)
    }

    fun updateTripName(tripName: String) {
        _tripFormData.value = tripFormData.value.copy(tripName =  tripName)
    }

    fun setTripVisibility(tripVisibility: TripVisibility) {
        _tripFormData.value = tripFormData.value.copy(visibility = tripVisibility)
    }

    fun addTripMates(user: User) {
        val newList = mutableListOf<User>()
        newList.addAll(tripFormData.value.tripMates)
        newList.add(user)
        _tripFormData.value = tripFormData.value.copy(tripMates = newList)
    }


    override fun processEvent(viewEvent: PostingInitIntent.ViewEvent) {
        when (viewEvent) {
            PostingInitIntent.ViewEvent.SaveAndContinue -> saveTheTripDataAndContinue()
        }
    }

    private fun saveTheTripDataAndContinue() {
        viewModelScope.launch {
            val postTripResult = SafeIOUtil.safeCall {
                postingUseCase.postTrip(_tripFormData.value)
            }
            postTripResult.onSuccess {
                _viewEffects.emit(PostingInitIntent.ViewEffect.GoNext(it))
            }
            postTripResult.onFailure {
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowError)
            }
        }
    }

}