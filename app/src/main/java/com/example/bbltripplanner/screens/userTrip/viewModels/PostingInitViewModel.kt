package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.BuildConfig
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.TripPlannerException
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
import retrofit2.HttpException

@OptIn(FlowPreview::class)
class PostingInitViewModel(
    tripId: String?,
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
            followers = inviteList.value?.followers?.filter { it.id != user.id } ?: emptyList(),
            isError = false
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
            PostingInitIntent.ViewEvent.RetryLocationSearch -> {
                val query = _searchQuery.value
                if (query.isNotBlank()) {
                    getLocationSuggestions(query)
                }
            }
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
                    _viewEffects.emit(PostingInitIntent.ViewEffect.ShowError(getFriendlyErrorMessage(it)))
                }
            }
        }
    }

    private fun getTripDetails(tripId: String) {
        viewModelScope.launch {
            _viewEffects.emit(PostingInitIntent.ViewEffect.ShowLoading)
            val tripDetailDeferred = async {
                SafeIOUtil.safeCall {
                    userTripDetailUseCase.getUserTripDetail(tripId)
                }
            }
            val tripMembersDeferred = async {
                SafeIOUtil.safeCall {
                    userTripDetailUseCase.getTripMembers(tripId)
                }
            }

            val tripDetailResult = tripDetailDeferred.await()
            val tripMembersResult = tripMembersDeferred.await()

            tripDetailResult.onSuccess { tripData ->
                val members = (tripMembersResult.getOrNull()?.map { it.user } ?: tripData.invitedMembers).filter { it.id != authPreferencesUseCase.getUserIdLogged() }
                _viewEffects.emit(PostingInitIntent.ViewEffect.HideLoading)
                _tripFormData.value = tripData.copy(invitedMembers = members)

                _inviteList.value?.let { currentInviteList ->
                    val memberIds = members.map { it.id }.toSet()
                    _inviteList.value = currentInviteList.copy(
                        followers = currentInviteList.followers.filter { it.id !in memberIds }
                    )
                }
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
            followers = newInviteList,
            isError = false
        )
    }

    private fun getFriendlyErrorMessage(throwable: Throwable): String {
        return when {
            throwable is java.io.IOException -> Constants.ErrorType.NETWORK_ERROR
            throwable is TripPlannerException && throwable.errorCode == 403 -> Constants.ErrorType.NOT_AUTHORIZED
            throwable is TripPlannerException && throwable.errorCode in 500..599 -> Constants.ErrorType.SERVER_ERROR
            throwable is HttpException && throwable.code() == 403 -> Constants.ErrorType.NOT_AUTHORIZED
            throwable is HttpException && throwable.code() == 404 -> Constants.ErrorType.NO_LOCATION_AVAILABLE
            throwable is HttpException && throwable.code() in 500..599 -> Constants.ErrorType.SERVER_ERROR
            else -> throwable.message ?: Constants.DEFAULT_ERROR_MESSAGE
        }
    }

    private fun getLocationSuggestions(query: String) {
        viewModelScope.launch {
            val locationSuggestionsResult = SafeIOUtil.safeCall {
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowLocationLoading)
                locationSearchUseCase.getLocationSuggestions(BuildConfig.LOCATION_API_KEY, query)
            }
            locationSuggestionsResult.onSuccess {
                _viewEffects.emit(PostingInitIntent.ViewEffect.HideLocationLoading)
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowSuggestions(it, isError = false))
            }
            locationSuggestionsResult.onFailure {
                _viewEffects.emit(PostingInitIntent.ViewEffect.HideLocationLoading)
                _viewEffects.emit(
                    PostingInitIntent.ViewEffect.ShowSuggestions(
                        emptyList(),
                        isError = true,
                        errorMessage = getFriendlyErrorMessage(it)
                    )
                )
            }
        }
    }

    private fun getInviteList() {
        if (_inviteList.value != null && !_inviteList.value!!.isError) {
            val memberIds = _tripFormData.value.invitedMembers.map { it.id }.toSet()
            _inviteList.value = _inviteList.value?.copy(
                followers = _inviteList.value?.followers?.filter { it.id !in memberIds } ?: emptyList()
            )
            return
        }
        authPreferencesUseCase.getUserIdLogged()?.let { userId ->
            viewModelScope.launch {
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowFollowersLoading)
                val followersList = SafeIOUtil.safeCall {
                    profileRelationUseCase.getFollowers(userId)
                }
                followersList.onSuccess { followersData ->
                    _viewEffects.emit(PostingInitIntent.ViewEffect.HideFollowersLoading)
                    val memberIds = _tripFormData.value.invitedMembers.map { it.id }.toSet()
                    val filteredFollowers = followersData?.followers?.filter { it.id !in memberIds } ?: emptyList()
                    _inviteList.value = ProfileFollowersData(filteredFollowers, isError = false)
                }
                followersList.onFailure {
                    _viewEffects.emit(PostingInitIntent.ViewEffect.HideFollowersLoading)
                    _inviteList.value = ProfileFollowersData(emptyList(), isError = true, errorMessage = getFriendlyErrorMessage(it))
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
                _viewEffects.emit(PostingInitIntent.ViewEffect.ShowError(getFriendlyErrorMessage(it)))
            }
        }
    }

}