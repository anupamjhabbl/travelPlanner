package com.example.bbltripplanner.screens.home.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVPresenter
import com.example.bbltripplanner.screens.home.usecases.HomeCxeUseCase
import com.example.bbltripplanner.screens.user.profile.viewModels.OtherProfileIntent
import kotlinx.coroutines.launch

class HomeExperienceViewModel(
    private val homeCxeUseCase: HomeCxeUseCase
) : ViewModel(), BaseMVIVPresenter<OtherProfileIntent.ViewEvent> {

    override fun processEvent(viewEvent: OtherProfileIntent.ViewEvent) {}

    private fun getCxeResponse() {
        viewModelScope.launch {
            kotlin.runCatching {
                homeCxeUseCase.getHomeCxeResponse()
            }.onSuccess {

            }.onFailure {

            }
        }
    }

}