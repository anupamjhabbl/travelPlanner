package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileRelationUsecase
import com.example.bbltripplanner.screens.userTrip.entity.TripMember
import com.example.bbltripplanner.screens.userTrip.entity.TripMemberStatus
import com.example.bbltripplanner.screens.userTrip.usecases.UserTripDetailUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class TripGroupViewModel(
    private val tripId: String,
    private val userTripDetailUseCase: UserTripDetailUseCase,
    private val profileRelationUseCase: ProfileRelationUsecase,
    private val authPreferencesUseCase: AuthPreferencesUseCase
) : BaseMVIVViewModel<TripGroupIntent.ViewEvent>() {

    private val _viewState = MutableStateFlow(TripGroupIntent.ViewState())
    val viewState: StateFlow<TripGroupIntent.ViewState> = _viewState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<TripGroupIntent.ViewEffect>()
    val viewEffect: SharedFlow<TripGroupIntent.ViewEffect> = _viewEffect.asSharedFlow()

    private var cachedFollowers: List<User>? = null

    init {
        processEvent(TripGroupIntent.ViewEvent.GetTripMembers)
    }

    override fun processEvent(viewEvent: TripGroupIntent.ViewEvent) {
        when (viewEvent) {
            TripGroupIntent.ViewEvent.GetTripMembers -> getTripMembers()
            TripGroupIntent.ViewEvent.GetInviteList -> getInviteList()
            is TripGroupIntent.ViewEvent.AddMember -> addMember(viewEvent.user)
        }
    }

    private fun getTripMembers() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true, error = null) }
            val result = SafeIOUtil.safeCall {
                userTripDetailUseCase.getTripMembers(tripId)
            }
            result.onSuccess { members ->
                _viewState.update { it.copy(isLoading = false, tripMembers = members, error = null) }
            }
            result.onFailure { exception ->
                val errorMsg = when {
                    exception is java.io.IOException -> Constants.ErrorType.NETWORK_ERROR
                    exception is HttpException && exception.code() == 404 -> Constants.ErrorType.NOT_FOUND
                    exception is HttpException && exception.code() == 403 -> Constants.ErrorType.NOT_AUTHORIZED
                    exception is TripPlannerException && exception.errorCode in 500..599 -> Constants.ErrorType.SERVER_ERROR
                    exception is TripPlannerException -> exception.message ?: Constants.ErrorType.SERVER_ERROR
                    else -> Constants.ErrorType.SERVER_ERROR
                }
                _viewState.update { it.copy(isLoading = false, error = errorMsg) }
            }
        }
    }

    private fun getInviteList() {
        val currentUserId = authPreferencesUseCase.getUserIdLogged() ?: return
        
        cachedFollowers?.let { followers ->
            val existingUserIds = _viewState.value.tripMembers.map { it.user.id }.toSet()
            val inviteList = followers.filter { it.id !in existingUserIds }
            _viewState.update { it.copy(inviteList = inviteList, isFollowersError = false, followersErrorMessage = null) }
            return
        }

        if (_viewState.value.isFollowersLoading) return

        viewModelScope.launch {
            _viewState.update { it.copy(isFollowersLoading = true, isFollowersError = false, followersErrorMessage = null) }
            val result = SafeIOUtil.safeCall {
                profileRelationUseCase.getFollowers(currentUserId)
            }
            result.onSuccess { followersData ->
                val followers = followersData?.followers ?: emptyList()
                cachedFollowers = followers
                val existingUserIds = _viewState.value.tripMembers.map { it.user.id }.toSet()
                val inviteList = followers.filter { it.id !in existingUserIds }
                _viewState.update { 
                    it.copy(
                        isFollowersLoading = false, 
                        inviteList = inviteList, 
                        isFollowersError = false, 
                        followersErrorMessage = null
                    ) 
                }
            }
            result.onFailure { exception ->
                val errorMsg = when {
                    exception is java.io.IOException -> Constants.ErrorType.NETWORK_ERROR
                    exception is HttpException && exception.code() == 404 -> Constants.ErrorType.NOT_FOUND
                    exception is HttpException && exception.code() == 403 -> Constants.ErrorType.NOT_AUTHORIZED
                    exception is TripPlannerException && exception.errorCode in 500..599 -> Constants.ErrorType.SERVER_ERROR
                    exception is TripPlannerException -> exception.message ?: Constants.ErrorType.SERVER_ERROR
                    else -> Constants.ErrorType.SERVER_ERROR
                }
                _viewState.update { 
                    it.copy(
                        isFollowersLoading = false, 
                        isFollowersError = true, 
                        followersErrorMessage = errorMsg
                    ) 
                }
            }
        }
    }

    private fun addMember(user: User) {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }
            val result = SafeIOUtil.safeCall {
                userTripDetailUseCase.addTripMember(tripId, user.id)
            }
            result.onSuccess {
                _viewState.update {
                    it.copy(
                        isLoading = false,
                        tripMembers = it.tripMembers.toMutableList().plus(TripMember(user, TripMemberStatus.PENDING))
                    )
                }
                _viewEffect.emit(TripGroupIntent.ViewEffect.ShowSuccess)
            }
            result.onFailure { exception ->
                _viewState.update { it.copy(isLoading = false) }
                val errorMsg = when {
                    exception is java.io.IOException -> Constants.ErrorType.NETWORK_ERROR
                    exception is HttpException && exception.code() == 404 -> Constants.ErrorType.NOT_FOUND
                    exception is HttpException && exception.code() == 403 -> Constants.ErrorType.NOT_AUTHORIZED
                    exception is TripPlannerException && exception.errorCode in 500..599 -> Constants.ErrorType.SERVER_ERROR
                    exception is TripPlannerException -> exception.message ?: Constants.ErrorType.SERVER_ERROR
                    else -> Constants.ErrorType.SERVER_ERROR
                }
                _viewEffect.emit(TripGroupIntent.ViewEffect.ShowError(errorMsg))
            }
        }
    }
}
