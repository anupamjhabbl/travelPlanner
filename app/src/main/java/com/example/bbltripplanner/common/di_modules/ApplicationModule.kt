package com.example.bbltripplanner.common.di_modules

import com.example.bbltripplanner.common.infra.Network
import com.example.bbltripplanner.screens.home.usecases.HomeCxeUseCase
import com.example.bbltripplanner.screens.home.viewModels.HomeExperienceViewModel
import com.example.bbltripplanner.screens.user.myacount.viewModels.MyAccountViewModel
import com.example.bbltripplanner.screens.user.profile.clients.UserClient
import com.example.bbltripplanner.screens.user.profile.repositories.GetProfileRepository
import com.example.bbltripplanner.screens.user.profile.repositoryimpl.GetProfileNetwork
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileUseCase
import com.example.bbltripplanner.screens.user.profile.viewModels.OtherProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserClient> { Network.create(UserClient::class.java) }
    single<GetProfileRepository> { GetProfileNetwork(get()) }
    single<ProfileUseCase> { ProfileUseCase(get()) }
    single<HomeCxeUseCase> { HomeCxeUseCase() }
    viewModel { OtherProfileViewModel(get()) }
    viewModel { MyAccountViewModel() }
    viewModel { HomeExperienceViewModel(get()) }
}