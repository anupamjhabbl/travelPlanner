package com.example.bbltripplanner.screens.user.profile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.profile.clients.UserClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlockedUsersViewModel(
    private val userClient: UserClient
): ViewModel() {
    private val _blockedUsers: MutableStateFlow<RequestResponseStatus<List<User>>> = MutableStateFlow(RequestResponseStatus(isLoading = true))
    val blockedUsers: StateFlow<RequestResponseStatus<List<User>>> = _blockedUsers.asStateFlow()

    init {
        fetchBlockedUsers()
    }

    private fun fetchBlockedUsers() {
        viewModelScope.launch {
            _blockedUsers.value = _blockedUsers.value.copy(isLoading = true)
            val result = SafeIOUtil.safeCall {
                BaseResponse.processResponse { userClient.getBlockedUsers() }
            }
            result.onSuccess { users ->
                _blockedUsers.value = _blockedUsers.value.copy(
                    isLoading = false,
                    data = users
                )
            }
            result.onFailure { exception ->
                val errorMessage = if (exception is TripPlannerException) exception.message else Constants.DEFAULT_ERROR
                _blockedUsers.value = _blockedUsers.value.copy(isLoading = false, error = errorMessage)
            }
        }
    }

    fun unblockUser(userId: String) {
        viewModelScope.launch {
            val result = SafeIOUtil.safeCall {
                BaseResponse.processResponse { userClient.unblockUser(userId) }
            }
            result.onSuccess {
                val currentList = _blockedUsers.value.data?.toMutableList()
                currentList?.removeAll { it.id == userId }
                _blockedUsers.value = _blockedUsers.value.copy(data = currentList)
            }
        }
    }
}