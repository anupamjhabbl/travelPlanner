package com.example.bbltripplanner.screens.user.profile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.entity.TripPlannerException
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileRelationUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileFollowersViewModel(
    private val profileRelationUsecase: ProfileRelationUsecase,
    private val userId: String?
): ViewModel() {
    private val _userList: MutableStateFlow<RequestResponseStatus<List<User>>> = MutableStateFlow(RequestResponseStatus(isLoading = true))
    val userList: StateFlow<RequestResponseStatus<List<User>>> = _userList.asStateFlow()

    init {
        fetchFollowers()
    }

    private fun fetchFollowers() {
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
                    if (exception is TripPlannerException) {
                        _userList.value = _userList.value.copy(isLoading = false, error = exception.message)
                    } else {
                        _userList.value = _userList.value.copy(isLoading = false, error = Constants.DEFAULT_ERROR)
                    }
                }
            }
        }
    }
}