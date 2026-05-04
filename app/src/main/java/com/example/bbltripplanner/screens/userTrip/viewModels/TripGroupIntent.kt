package com.example.bbltripplanner.screens.userTrip.viewModels

import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.userTrip.entity.TripMember

class TripGroupIntent {
    sealed class ViewEvent {
        data object GetTripMembers : ViewEvent()
        data object GetInviteList : ViewEvent()
        data class AddMember(val user: User) : ViewEvent()
    }

    sealed class ViewEffect {
        data class ShowError(val message: String) : ViewEffect()
        data object ShowSuccess : ViewEffect()
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val tripMembers: List<TripMember> = emptyList(),
        val inviteList: List<User> = emptyList(),
        val isFollowersLoading: Boolean = false,
        val error: String? = null
    )
}
