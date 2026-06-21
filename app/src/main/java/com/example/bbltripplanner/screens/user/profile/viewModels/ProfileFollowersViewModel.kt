package com.example.bbltripplanner.screens.user.profile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.ErrorUtils
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.profile.entity.ProfileFollow
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileRelationUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileFollowersViewModel(
    private val profileRelationUsecase: ProfileRelationUsecase,
    private val authPreferencesUseCase: AuthPreferencesUseCase,
    private val userId: String?
): ViewModel() {
    private val _userList: MutableStateFlow<RequestResponseStatus<List<User>>> = MutableStateFlow(RequestResponseStatus(isLoading = true))
    val userList: StateFlow<RequestResponseStatus<List<User>>> = _userList.asStateFlow()

    init {
        fetchFollowers()
    }

    fun isSelfProfile(): Boolean {
        return authPreferencesUseCase.getUserIdLogged() == userId
    }

    fun fetchFollowers() {
        userId?.let {
            viewModelScope.launch {
                _userList.value = _userList.value.copy(isLoading = true)
                val result = SafeIOUtil.safeCall {
                    profileRelationUsecase.getFollowers(it)
                }
                result.onSuccess { result ->
                    _userList.value = _userList.value.copy(
                        isLoading = false,
                        data = result?.followers
                    )
                }
                result.onFailure { exception ->
                    val errorMsg = ErrorUtils.toErrorType(exception)
                    _userList.value = _userList.value.copy(isLoading = false, error = errorMsg)
                }
            }
        }
    }

    fun followUser(targetUserId: String) {
        viewModelScope.launch {
            SafeIOUtil.safeCall {
                profileRelationUsecase.followUser(ProfileFollow(targetUserId))
            }
            val currentList = _userList.value.data?.map { 
                if (it.id == targetUserId) it.copy(isFollowing = true) else it 
            }
            _userList.value = _userList.value.copy(data = currentList)
        }
    }

    fun updateFollowStatusLocal(targetUserId: String, isFollowing: Boolean) {
        val currentList = _userList.value.data?.map { 
            if (it.id == targetUserId) it.copy(isFollowing = isFollowing) else it 
        }
        _userList.value = _userList.value.copy(data = currentList)
    }
}