package com.example.bbltripplanner.screens.user.profile.viewModels

import com.example.bbltripplanner.common.entity.User
import okhttp3.MultipartBody

class EditProfileIntent {
    sealed interface ViewEvent {
        data class UpdateUser(val user: User, val imageFile: MultipartBody.Part?): ViewEvent
    }
}