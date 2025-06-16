package com.example.bbltripplanner.common.di_modules

import com.example.bbltripplanner.common.infra.Network
import com.example.bbltripplanner.user.profile.clients.UserClient
import com.example.bbltripplanner.user.profile.repositories.GetProfileRepository
import com.example.bbltripplanner.user.profile.repositoryimpl.GetProfileNetwork
import com.example.bbltripplanner.user.profile.usecases.ProfileUseCase
import com.example.bbltripplanner.user.profile.viewModels.OtherProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserClient> { Network.create(UserClient::class.java) }
    single<GetProfileRepository> { GetProfileNetwork(get()) }
    single<ProfileUseCase> { ProfileUseCase(get()) }
    viewModel { OtherProfileViewModel(get()) }
}