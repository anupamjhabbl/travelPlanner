package com.example.bbltripplanner.screens.userTrip.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
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
            _viewState.update { it.copy(isLoading = true) }
            val result = SafeIOUtil.safeCall {
                userTripDetailUseCase.getTripMembers(tripId)
            }
            result.onSuccess { members ->
                _viewState.update { it.copy(isLoading = false, tripMembers = members) }
            }
            result.onFailure { error ->
                _viewState.update { it.copy(isLoading = false, error = error.message) }
                _viewEffect.emit(TripGroupIntent.ViewEffect.ShowError(error.message ?: "Failed to fetch members"))
            }
        }
    }

    private fun getInviteList() {
        val currentUserId = authPreferencesUseCase.getUserIdLogged() ?: return
        viewModelScope.launch {
            _viewState.update { it.copy(isFollowersLoading = true) }
            val result = SafeIOUtil.safeCall {
                profileRelationUseCase.getFollowers(currentUserId)
            }
            result.onSuccess { followersData ->
                val existingUserIds = _viewState.value.tripMembers.map { it.user.id }.toSet()
                val inviteList = followersData?.followers?.filter { it.id !in existingUserIds } ?: emptyList()
                _viewState.update { it.copy(isFollowersLoading = false, inviteList = inviteList) }
            }
            result.onFailure {
                _viewState.update { it.copy(isFollowersLoading = false) }
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
            result.onFailure {
                _viewState.update { it.copy(isLoading = false) }
                _viewEffect.emit(TripGroupIntent.ViewEffect.ShowError(it.message ?: "Failed to add member"))
            }
        }
    }
}
