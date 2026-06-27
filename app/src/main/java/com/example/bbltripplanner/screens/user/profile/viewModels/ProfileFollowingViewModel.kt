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

class ProfileFollowingViewModel(
    private val profileRelationUsecase: ProfileRelationUsecase,
    private val authPreferencesUseCase: AuthPreferencesUseCase,
    private val userId: String?
): ViewModel() {
    private val _userList: MutableStateFlow<RequestResponseStatus<List<User>>> = MutableStateFlow(RequestResponseStatus(isLoading = true))
    val userList: StateFlow<RequestResponseStatus<List<User>>> = _userList.asStateFlow()

    init {
        fetchFollowing()
    }

    fun isSelfProfile(): Boolean {
        return authPreferencesUseCase.getUserIdLogged() == userId
    }

    fun fetchFollowing() {
        userId?.let {
            viewModelScope.launch {
                _userList.value = _userList.value.copy(isLoading = true)
                val result = SafeIOUtil.safeCall {
                    profileRelationUsecase.getFollowings(it)
                }
                result.onSuccess { result ->
                    _userList.value = RequestResponseStatus(
                        isLoading = false,
                        data = result?.following
                    )
                }
                result.onFailure { exception ->
                    val errorMsg = ErrorUtils.toErrorType(exception)
                    _userList.value = _userList.value.copy(isLoading = false, error = errorMsg)
                }
            }
        }
    }

    fun unfollowUser(targetUserId: String) {
        viewModelScope.launch {
            SafeIOUtil.safeCall {
                profileRelationUsecase.unfollowUser(ProfileFollow(targetUserId))
            }
            val currentList = _userList.value.data?.toMutableList()
            currentList?.removeAll { it.id == targetUserId }
            _userList.value = _userList.value.copy(data = currentList)
        }
    }

    fun addFollowedUserLocal(user: User) {
        val currentList = _userList.value.data?.toMutableList() ?: mutableListOf()
        if (currentList.none { it.id == user.id }) {
            currentList.add(user.copy(isFollowing = true))
            _userList.value = _userList.value.copy(data = currentList)
        }
    }
}