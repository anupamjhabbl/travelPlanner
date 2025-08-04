package com.example.bbltripplanner.screens.home.viewModels

import androidx.lifecycle.ViewModel
import com.example.bbltripplanner.common.baseClasses.BaseMVIVPresenter
import com.example.bbltripplanner.screens.home.usecases.HomeCxeUseCase
import com.example.bbltripplanner.screens.user.profile.viewModels.OtherProfileIntent

class HomeExperienceViewModel(
    val homeCxeUseCase: HomeCxeUseCase
) : ViewModel(), BaseMVIVPresenter<OtherProfileIntent.ViewEvent> {

    override fun processEvent(viewEvent: OtherProfileIntent.ViewEvent) {}

}