package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.BuildConfig
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollowersData
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileRelationUsecase
import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.screens.userTrip.entity.TripCreationResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.TripVisibility
import com.example.bbltripplanner.screens.userTrip.usecases.LocationSearchUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.PostingUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.UserTripDetailUseCase
import kotlinx.coroutines.FlowPreview
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

@OptIn(FlowPreview::class)
class PostingInitViewModel(
    private val tripId: String?,
    private val postingUseCase: PostingUseCase,
    private val locationSearchUseCase: LocationSearchUseCase,
    private val profileRelationUseCase: ProfileRelationUsecase,
    private val authPreferencesUseCase: AuthPreferencesUseCase,
    private val userTripDetailUseCase: UserTripDetailUseCase
): BaseMVIVViewModel<PostingInitIntent.ViewEvent>() {
    private val _tripFormData: MutableStateFlow<TripData> = MutableStateFlow(TripData())
    val tripFormData: StateFlow<TripData> = _tripFormData.asStateFlow()

    private val _inviteList: MutableStateFlow<ProfileFollowersData?> = MutableStateFlow(null)
    val inviteList: StateFlow<ProfileFollowersData?> = _inviteList.asStateFlow()

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
                    getLocationSuggestions(it)
                }
        }
        if (tripId != null) {
            processEvent(PostingInitIntent.ViewEvent.GetTripDetails(tripId))
        }
    }

    private fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun updateTripLocation(location: Location) {
        _tripFormData.value = tripFormData.value.copy(whereTo = location)
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

    private fun addTripMates(user: User) {
        val newList = mutableListOf<User>()
        newList.addAll(tripFormData.value.invitedMembers)
        newList.add(user)
        _tripFormData.value = tripFormData.value.copy(invitedMembers = newList)
        _inviteList.value = inviteList.value?.copy(
            followers = inviteList.value?.followers?.filter { it.id != user.id } ?: emptyList()
        )
    }


    override fun processEvent(viewEvent: PostingInitIntent.ViewEvent) {
        when (viewEvent) {
            PostingInitIntent.ViewEvent.SaveAndContinue -> saveTheTripDataAndContinue()
            PostingInitIntent.ViewEvent.UpdateAndContinue -> updateTheTripDataAndContinue()
            PostingInitIntent.ViewEvent.GetInviteList -> getInviteList()
            is PostingInitIntent.ViewEvent.OnQueryChanged -> onQueryChanged(viewEvent.query)
            is PostingInitIntent.ViewEvent.UpdateEndDate -> updateTripEndDate(viewEvent.endDate)
            is PostingInitIntent.ViewEvent.UpdateStartDate -> updateTripStartDate(viewEvent.startDate)
            is PostingInitIntent.ViewEvent.UpdateTripLocation -> updateTripLocation(viewEvent.location)
            is PostingInitIntent.ViewEvent.UpdateTripName -> updateTripName(viewEvent.tripName)
            is PostingInitIntent.ViewEvent.SetTripVisibility -> setTripVisibility(viewEvent.tripVisibility)
            is PostingInitIntent.ViewEvent.AddTripMates -> addTripMates(viewEvent.user)
            is PostingInitIntent.ViewEvent.RemoveTripMates -> removeTripMates(viewEvent.user)
            is PostingInitIntent.ViewEvent.GetTripDetails -> getTripDetails(viewEvent.tripId)
        }
    }

    private fun updateTheTripDataAndContinue() {
        val tripId = _tripFormData.value.tripId
        if (tripId != null) {
            viewModelScope.launch {
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowLoading)
                val postTripResult = SafeIOUtil.safeCall {
                    postingUseCase.updateTrip(tripId, _tripFormData.value)
                }
                postTripResult.onSuccess {
                    _viewEffects.emit(PostingInitIntent.ViewEffect.HideLoading)
                    _viewEffects.emit(PostingInitIntent.ViewEffect.GoNext(TripCreationResponse(tripId)))
                }
                postTripResult.onFailure {
                    _viewEffects.emit(PostingInitIntent.ViewEffect.HideLoading)
                    _viewEffects.emit(PostingInitIntent.ViewEffect.ShowError)
                }
            }
        }
    }

    private fun getTripDetails(tripId: String) {
        viewModelScope.launch {
            _viewEffects.emit(PostingInitIntent.ViewEffect.ShowLoading)
            val tripDetailResult = SafeIOUtil.safeCall {
                userTripDetailUseCase.getUserTripDetail(tripId)
            }
            tripDetailResult.onSuccess { tripData ->
                _viewEffects.emit(PostingInitIntent.ViewEffect.HideLoading)
                _tripFormData.value = tripData
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowSuccess)
            }
            tripDetailResult.onFailure {
                _viewEffects.emit(PostingInitIntent.ViewEffect.HideLoading)
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowFullScreenError(it.message ?: ""))
            }
        }
    }

    private fun removeTripMates(user: User) {
        val newInviteList = mutableListOf<User>()
        newInviteList.addAll(inviteList.value?.followers ?: emptyList())
        newInviteList.add(user)
        _tripFormData.value = tripFormData.value.copy(
            invitedMembers = tripFormData.value.invitedMembers.filter { it.id != user.id }
        )
        _inviteList.value = inviteList.value?.copy(
            newInviteList
        )
    }

    private fun getLocationSuggestions(query: String) {
        viewModelScope.launch {
            val locationSuggestionsResult = SafeIOUtil.safeCall {
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowLocationLoading)
                locationSearchUseCase.getLocationSuggestions(BuildConfig.LOCATION_API_KEY, query)
            }
            locationSuggestionsResult.onSuccess {
                _viewEffects.emit(PostingInitIntent.ViewEffect.HideLocationLoading)
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowSuggestions(it))
            }
            locationSuggestionsResult.onFailure {
                _viewEffects.emit(PostingInitIntent.ViewEffect.HideLocationLoading)
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowSuggestions(emptyList()))
            }
        }
    }

    private fun getInviteList() {
        if (_inviteList.value != null) {
            return
        }
        authPreferencesUseCase.getUserIdLogged()?.let { userId ->
            viewModelScope.launch {
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowFollowersLoading)
                val followersList = SafeIOUtil.safeCall {
                    profileRelationUseCase.getFollowers(userId)
                }
                followersList.onSuccess { followers ->
                    _viewEffects.emit(PostingInitIntent.ViewEffect.HideFollowersLoading)
                    _inviteList.value = followers
                }
                followersList.onFailure {
                    _viewEffects.emit(PostingInitIntent.ViewEffect.HideFollowersLoading)
                    _inviteList.value = ProfileFollowersData(emptyList())
                }
            }
        }
    }

    private fun saveTheTripDataAndContinue() {
        viewModelScope.launch {
            _viewEffects.emit(PostingInitIntent.ViewEffect.ShowLoading)
            val postTripResult = SafeIOUtil.safeCall {
                postingUseCase.postTrip(_tripFormData.value)
            }
            postTripResult.onSuccess {
                _viewEffects.emit(PostingInitIntent.ViewEffect.HideLoading)
                _viewEffects.emit(PostingInitIntent.ViewEffect.GoNext(it))
            }
            postTripResult.onFailure {
                _viewEffects.emit(PostingInitIntent.ViewEffect.HideLoading)
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowError)
            }
        }
    }

}