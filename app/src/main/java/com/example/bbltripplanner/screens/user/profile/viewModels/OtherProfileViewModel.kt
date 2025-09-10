package com.example.bbltripplanner.screens.user.profile.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OtherProfileViewModel(
    private val profileUseCase: ProfileUseCase
): BaseMVIVViewModel<OtherProfileIntent.ViewEvent>() {

    private val _viewEffect = MutableStateFlow<OtherProfileIntent.ViewEffect?>(null)
    val viewEffect = _viewEffect.asStateFlow()

    private fun fetchProfileData() {
        viewModelScope.launch {
            kotlin.runCatching {
                profileUseCase.getOwnProfile()
            }.onSuccess {
                _viewEffect.value = OtherProfileIntent.ViewEffect.ShowUser(it)
            }.onFailure {
                _viewEffect.value = OtherProfileIntent.ViewEffect.UserFailure
            }
        }
    }

    override fun processEvent(viewEvent: OtherProfileIntent.ViewEvent) {
        when (viewEvent) {
            OtherProfileIntent.ViewEvent.FetchUserData -> fetchProfileData()
        }
    }
}