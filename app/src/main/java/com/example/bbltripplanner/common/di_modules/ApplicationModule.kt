package com.example.bbltripplanner.common.di_modules

import com.example.bbltripplanner.common.infra.EncryptedPreferenceManager
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
import com.example.bbltripplanner.screens.user.auth.clients.UserAuthClient
import com.example.bbltripplanner.screens.user.auth.repositories.UserAuthRepository
import com.example.bbltripplanner.screens.user.auth.repositoryImpl.UserAuthNetwork
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.auth.usecases.UserAuthUseCase
import com.example.bbltripplanner.screens.user.auth.viewModels.ForgotPasswordAuthViewModel
import com.example.bbltripplanner.screens.user.auth.viewModels.OTPAuthViewModel
import com.example.bbltripplanner.screens.user.auth.viewModels.PasswordResetVieModel
import com.example.bbltripplanner.screens.user.auth.viewModels.UseRegistrationViewModel
import com.example.bbltripplanner.screens.user.auth.viewModels.UserLoginAuthViewModel
import com.example.bbltripplanner.screens.user.myacount.viewModels.MyAccountViewModel
import com.example.bbltripplanner.screens.user.profile.clients.UserClient
import com.example.bbltripplanner.screens.user.profile.repositories.GetProfileRepository
import com.example.bbltripplanner.screens.user.profile.repositoryImpl.GetProfileNetwork
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileUseCase
import com.example.bbltripplanner.screens.user.profile.viewModels.OtherProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // infra
    single<EncryptedPreferenceManager> { EncryptedPreferenceManager(androidContext()) }

    // User
    single<UserClient> { Network.createWithAuth(UserClient::class.java, get(), get()) }
    single<GetProfileRepository> { GetProfileNetwork(get()) }
    single<ProfileUseCase> { ProfileUseCase(get()) }
    viewModel { OtherProfileViewModel(get()) }
    viewModel { MyAccountViewModel() }

    // Home & Listing
    single<HomeCxeLayoutRepository> { HomeCxeLayoutNetwork(get()) }
    single<HomeCxeUseCase> { HomeCxeUseCase(get()) }
    single<HomeCxeClient> { Network.createWithAuth(HomeCxeClient::class.java, get(), get()) }
    viewModel { HomeExperienceViewModel(get()) }

    // Posting & Trips
    single<PostingClient> { Network.createWithAuth(PostingClient::class.java, get(), get()) }
    single<PostingRepository> { PostingNetwork(get()) }
    single<PostingUseCase> { PostingUseCase(get()) }
    single<UserTripDetailUseCase> { UserTripDetailUseCase(get()) }
    single<UserTripDetailRepository> { UseTripDetailNetwork(get()) }
    single<UserTripDetailClient> { Network.createWithAuth(UserTripDetailClient::class.java, get(), get()) }
    viewModel { PostingInitViewModel(get()) }
    viewModel { UserTripDetailViewModel(get()) }

    // User Auth
    single<UserAuthClient> { Network.create(UserAuthClient::class.java) }
    single<UserAuthRepository> { UserAuthNetwork(get()) }
    single<UserAuthUseCase> { UserAuthUseCase(get()) }
    single<AuthPreferencesUseCase> { AuthPreferencesUseCase(get()) }
    viewModel { OTPAuthViewModel(get(), get()) }
    viewModel { UserLoginAuthViewModel(get(), get()) }
    viewModel { UseRegistrationViewModel(get()) }
    viewModel { ForgotPasswordAuthViewModel(get()) }
    viewModel { PasswordResetVieModel(get(), get()) }
}