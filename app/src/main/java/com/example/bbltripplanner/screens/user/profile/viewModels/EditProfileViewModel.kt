package com.example.bbltripplanner.screens.user.profile.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.entity.UpdateUserData
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EditProfileViewModel(
    private val authPreferencesUseCase: AuthPreferencesUseCase,
    private val profileUseCase: ProfileUseCase
): BaseMVIVViewModel<EditProfileIntent.ViewEvent>() {
    private val user: User? = authPreferencesUseCase.getLoggedUser()
    private val _userUpdateStatus: MutableSharedFlow<RequestStatus<String>> = MutableSharedFlow()
    val userUpdateStatus: SharedFlow<RequestStatus<String>> = _userUpdateStatus.asSharedFlow()

    override fun processEvent(viewEvent: EditProfileIntent.ViewEvent) {
        when (viewEvent) {
            is EditProfileIntent.ViewEvent.UpdateUser -> editedUser(viewEvent.user, viewEvent.imageFile)
        }
    }

    private fun editedUser(user: User, imageFile: MultipartBody.Part?) {
        var updateUserData: UpdateUserData? = null
        if (user.name == this.user?.name && user.bio == this.user.bio && user.phone == this.user.phone && imageFile == null) {
            viewModelScope.launch {
                _userUpdateStatus.emit(RequestStatus.Success("You didn't made any changes"))
            }
            return
        }
        if (user.name != this.user?.name || user.bio != this.user.bio || user.phone != this.user.phone) {
            updateUserData = UpdateUserData(
                name = user.name,
                bio = user.bio,
                phone = user.phone
            )
        }
        uploadEditedUser(updateUserData, imageFile)
    }

    private fun uploadEditedUser(updateUserData: UpdateUserData?, profilePic: MultipartBody.Part?) {
        viewModelScope.launch {
            _userUpdateStatus.emit(RequestStatus.Loading)
            val userUpdateRequest = SafeIOUtil.safeCall {
                profileUseCase.updateUser(updateUserData, profilePic)
            }
            userUpdateRequest.onSuccess { user ->
                if (user != null) {
                    authPreferencesUseCase.saveLoggedUser(user)
                }
                _userUpdateStatus.emit(RequestStatus.Success("User updated successfully"))
            }
            userUpdateRequest.onFailure { exception ->
                if (exception is TripPlannerException) {
                    _userUpdateStatus.emit(RequestStatus.Error(exception.message))
                } else {
                    _userUpdateStatus.emit(RequestStatus.Error(Constants.DEFAULT_ERROR))
                }
            }
        }
    }

    fun getUser(): User? {
        return user
    }
}