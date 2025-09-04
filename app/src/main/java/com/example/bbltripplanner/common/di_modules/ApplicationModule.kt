package com.example.bbltripplanner.common.di_modules

import com.example.bbltripplanner.common.infra.Network
import com.example.bbltripplanner.screens.home.clients.HomeCxeClient
import com.example.bbltripplanner.screens.home.repositories.HomeCxeLayoutRepository
import com.example.bbltripplanner.screens.home.repositoryImpl.HomeCxeLayoutNetwork
import com.example.bbltripplanner.screens.home.usecases.HomeCxeUseCase
import com.example.bbltripplanner.screens.home.viewModels.HomeExperienceViewModel
import com.example.bbltripplanner.screens.posting.clients.PostingClient
import com.example.bbltripplanner.screens.posting.clients.UserTripDetailClient
import com.example.bbltripplanner.screens.posting.repositories.PostingRepository
import com.example.bbltripplanner.screens.posting.repositories.UserTripDetailRepository
import com.example.bbltripplanner.screens.posting.repositoryImpl.PostingNetwork
import com.example.bbltripplanner.screens.posting.repositoryImpl.UseTripDetailNetwork
import com.example.bbltripplanner.screens.posting.usecases.PostingUseCase
import com.example.bbltripplanner.screens.posting.usecases.UserTripDetailUseCase
import com.example.bbltripplanner.screens.posting.viewModels.PostingInitViewModel
import com.example.bbltripplanner.screens.posting.viewModels.UserTripDetailViewModel
import com.example.bbltripplanner.screens.user.myacount.viewModels.MyAccountViewModel
import com.example.bbltripplanner.screens.user.profile.clients.UserClient
import com.example.bbltripplanner.screens.user.profile.repositories.GetProfileRepository
import com.example.bbltripplanner.screens.user.profile.repositoryImpl.GetProfileNetwork
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileUseCase
import com.example.bbltripplanner.screens.user.profile.viewModels.OtherProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserClient> { Network.create(UserClient::class.java) }
    single<HomeCxeClient> { Network.create(HomeCxeClient::class.java) }
    single<GetProfileRepository> { GetProfileNetwork(get()) }
    single<ProfileUseCase> { ProfileUseCase(get()) }
    single<HomeCxeLayoutRepository> { HomeCxeLayoutNetwork(get()) }
    single<HomeCxeUseCase> { HomeCxeUseCase(get()) }
    single<PostingClient> { Network.create(PostingClient::class.java) }
    single<PostingRepository> { PostingNetwork(get()) }
    single<PostingUseCase> { PostingUseCase(get()) }
    single<UserTripDetailUseCase> { UserTripDetailUseCase(get()) }
    single<UserTripDetailRepository> { UseTripDetailNetwork(get()) }
    single<UserTripDetailClient> { Network.create(UserTripDetailClient::class.java) }
    viewModel { OtherProfileViewModel(get()) }
    viewModel { MyAccountViewModel() }
    viewModel { HomeExperienceViewModel(get()) }
    viewModel { PostingInitViewModel(get()) }
    viewModel { UserTripDetailViewModel(get()) }
}