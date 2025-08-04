package com.example.bbltripplanner.screens.user.profile.viewModels

import com.example.bbltripplanner.common.entity.User

class OtherProfileIntent {
    sealed class ViewEffect {
        data class ShowUser(val user: User) : ViewEffect()
        data object UserFailure : ViewEffect() {

        }
    }

    sealed class ViewEvent {
        data object FetchUserData: ViewEvent()
    }
}