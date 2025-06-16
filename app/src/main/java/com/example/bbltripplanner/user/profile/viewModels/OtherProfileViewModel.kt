package com.example.bbltripplanner.user.profile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVPresenter
import com.example.bbltripplanner.user.profile.usecases.ProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class OtherProfileViewModel(
    private val profileUseCase: ProfileUseCase
): ViewModel(), BaseMVIVPresenter<OtherProfileIntent.ViewEvent> {

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