package com.example.bbltripplanner.screens.user.profile.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.MenuItems
import com.example.bbltripplanner.common.entity.RequestStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionItem
import com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionResourceMapper
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authPreferencesUseCase: AuthPreferencesUseCase,
    private val profileUseCase: ProfileUseCase
): BaseMVIVViewModel<ProfileIntent.ViewEvent>() {
    private val _userData: MutableStateFlow<RequestStatus<User>> = MutableStateFlow(RequestStatus.Idle)
    val userData: StateFlow<RequestStatus<User>> = _userData.asStateFlow()

    private val _viewState: MutableSharedFlow<ProfileIntent.ViewState> = MutableSharedFlow()
    val viewState: SharedFlow<ProfileIntent.ViewState> = _viewState.asSharedFlow()

    private lateinit var currentUser: User

    override fun processEvent(viewEvent: ProfileIntent.ViewEvent) {
        when (viewEvent) {
            is ProfileIntent.ViewEvent.SetUp -> setUp(viewEvent.userId)
            ProfileIntent.ViewEvent.BlockUser -> blockUser()
            ProfileIntent.ViewEvent.FollowUser -> followUser()
        }
    }

    private fun followUser() {
        val userDataValue =  userData.value
        if (userDataValue is RequestStatus.Success) {
            viewModelScope.launch {
                val followRequest = SafeIOUtil.safeCall {
                    profileUseCase.followUser(userDataValue.data.id)
                }

                followRequest.onSuccess {
                    _userData.value = RequestStatus.Success(userDataValue.data.copy(followersCount = userDataValue.data.followersCount + 1))
                    _viewState.emit(ProfileIntent.ViewState.FollowSuccess)
                }

                followRequest.onFailure {
                    _viewState.emit(ProfileIntent.ViewState.FollowFailure)
                }
            }
        }
    }

    private fun blockUser() {
        val userDataValue =  userData.value
        if (userDataValue is RequestStatus.Success) {
            viewModelScope.launch {
                val blockRequest = SafeIOUtil.safeCall {
                    profileUseCase.blockUser(userDataValue.data.id)
                }

                blockRequest.onSuccess {
                    _viewState.emit(ProfileIntent.ViewState.BlockSuccess)
                }

                blockRequest.onFailure {
                    _viewState.emit(ProfileIntent.ViewState.BlockFailure)
                }
            }
        }
    }

    private fun setUp(userId: String) {
        getProfileData(userId)
    }

    private fun getProfileData(userId: String) {
        if (isMyProfile(userId) && authPreferencesUseCase.isUserLogged()) {
            currentUser = authPreferencesUseCase.getLoggedUser()!!
            _userData.value =  RequestStatus.Success(currentUser)
        } else {
            _userData.value = RequestStatus.Loading
            fetchUserData(userId)
        }
    }

    private fun fetchUserData(userId: String) {
        viewModelScope.launch {
            val userDataRequest = SafeIOUtil.safeCall {
                profileUseCase.getUserProfile(userId)
            }
            userDataRequest.onSuccess { user ->
                if (user == null) {
                    _userData.value = RequestStatus.Error(Constants.DEFAULT_ERROR)
                } else {
                    currentUser = user
                    _userData.value = RequestStatus.Success(user)
                }
            }
            userDataRequest.onFailure { exception ->
                if (exception  is TripPlannerException) {
                    _userData.value = RequestStatus.Error(exception.message)
                } else {
                    _userData.value = RequestStatus.Error(Constants.DEFAULT_ERROR)
                }
            }
        }
    }

    fun getProfileActionList(): List<ProfileActionItem> {
        if (!this::currentUser.isInitialized) {
            return emptyList()
        }
        return if (isMyProfile(currentUser.id)) {
            ProfileActionResourceMapper.getMyProfileActions()
        } else  {
            ProfileActionResourceMapper.getOtherProfileActions()
        }
    }

    fun getProfileMenuItem(): List<String> {
        if (!this::currentUser.isInitialized) {
            return emptyList()
        }
        return if (isMyProfile(currentUser.id)) {
            MenuItems.MyProfileMenuItem.entries.map { it.value }
        } else {
            MenuItems.OtherProfileMenuItem.entries.map { it.value }
        }
    }

    private fun isMyProfile(userId: String): Boolean {
        return authPreferencesUseCase.getUserIdLogged() == userId
    }

    fun isMyProfile(): Boolean {
        return isMyProfile(currentUser.id)
    }
}