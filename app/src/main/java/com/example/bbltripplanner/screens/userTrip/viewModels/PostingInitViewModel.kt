package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowersData
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowingData
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileRelationUsecase
import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.TripVisibility
import com.example.bbltripplanner.screens.userTrip.usecases.LocationSearchUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.PostingUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

@OptIn(FlowPreview::class)
class PostingInitViewModel(
    private val postingUseCase: PostingUseCase,
    private val locationSearchUseCase: LocationSearchUseCase,
    private val profileRelationUseCase: ProfileRelationUsecase,
    private val authPreferencesUseCase: AuthPreferencesUseCase
): BaseMVIVViewModel<PostingInitIntent.ViewEvent>() {
    private val _tripFormData: MutableStateFlow<TripData> = MutableStateFlow(TripData())
    val tripFormData: StateFlow<TripData> = _tripFormData.asStateFlow()

    private val _viewEffects: MutableSharedFlow<PostingInitIntent.ViewEffect> = MutableSharedFlow()
    val viewEffect: SharedFlow<PostingInitIntent.ViewEffect> = _viewEffects.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(300L)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collect {
                    getLocationSuggestions("pk.dc4c3974f72cf55deadbe611b1ea1895", it)
                }
        }
    }

    private fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun updateTripLocation(location: Location) {
        _tripFormData.value = tripFormData.value.copy(tripLocation = location)
    }

    private fun updateTripStartDate(startDate: Long) {
        _tripFormData.value = tripFormData.value.copy(startDate = startDate)
    }

    private fun updateTripEndDate(endDate: Long) {
        _tripFormData.value = tripFormData.value.copy(endDate = endDate)
    }

    private fun updateTripName(tripName: String) {
        _tripFormData.value = tripFormData.value.copy(tripName =  tripName)
    }

    private fun setTripVisibility(tripVisibility: TripVisibility) {
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
            PostingInitIntent.ViewEvent.GetInviteList -> getInviteList()
            is PostingInitIntent.ViewEvent.OnQueryChanged -> onQueryChanged(viewEvent.query)
            is PostingInitIntent.ViewEvent.UpdateEndDate -> updateTripEndDate(viewEvent.endDate)
            is PostingInitIntent.ViewEvent.UpdateStartDate -> updateTripStartDate(viewEvent.startDate)
            is PostingInitIntent.ViewEvent.UpdateTripLocation -> updateTripLocation(viewEvent.location)
            is PostingInitIntent.ViewEvent.UpdateTripName -> updateTripName(viewEvent.tripName)
            is PostingInitIntent.ViewEvent.SetTripVisibility -> setTripVisibility(viewEvent.tripVisibility)
            is PostingInitIntent.ViewEvent.AddTripMates -> addTripMates(viewEvent.user)
        }
    }

    private fun getLocationSuggestions(key: String, query: String) {
        viewModelScope.launch {
            val locationSuggestionsResult = SafeIOUtil.safeCall {
                locationSearchUseCase.getLocationSuggestions(key, query)
            }
            locationSuggestionsResult.onSuccess {
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowSuggestions(it))
            }
            locationSuggestionsResult.onFailure {
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowSuggestions(emptyList()))
            }
        }
    }

    private fun getInviteList() {
        authPreferencesUseCase.getUserIdLogged()?.let { userId ->
            viewModelScope.launch {
                val followersList = SafeIOUtil.safeCall {
                    profileRelationUseCase.getFollowers(userId)
                }
                followersList.onSuccess { followers ->
                    _viewEffects.emit(PostingInitIntent.ViewEffect.InviteList(followers?.followers ?: emptyList()))
                }
                followersList.onFailure {
                    _viewEffects.emit(PostingInitIntent.ViewEffect.InviteList(emptyList()))
                }
            }
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