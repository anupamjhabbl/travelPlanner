package com.example.bbltripplanner.common.di_modules

import androidx.room.Room
import com.example.bbltripplanner.common.database.AppDatabase
import com.example.bbltripplanner.common.infra.EncryptedPreferenceManager
import com.example.bbltripplanner.common.infra.Network
import com.example.bbltripplanner.common.infra.PreferenceManager
import com.example.bbltripplanner.main.viewModels.MainActivityViewModel
import com.example.bbltripplanner.screens.home.clients.HomeCxeClient
import com.example.bbltripplanner.screens.home.repositories.HomeCxeLayoutRepository
import com.example.bbltripplanner.screens.home.repositoryImpl.HomeCxeLayoutNetwork
import com.example.bbltripplanner.screens.home.usecases.HomeCxeUseCase
import com.example.bbltripplanner.screens.home.viewModels.HomeExperienceViewModel
import com.example.bbltripplanner.screens.notification.client.NotificationClient
import com.example.bbltripplanner.screens.notification.repositories.NotificationRepository
import com.example.bbltripplanner.screens.notification.repositoryImpl.NotificationNetwork
import com.example.bbltripplanner.screens.notification.usecases.NotificationUseCase
import com.example.bbltripplanner.screens.notification.viewModels.NotificationViewModel
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
import com.example.bbltripplanner.screens.user.general.viewModels.HelpSupportViewModel
import com.example.bbltripplanner.screens.user.general.viewModels.UserSettingsViewModel
import com.example.bbltripplanner.screens.user.myacount.viewModels.MyAccountViewModel
import com.example.bbltripplanner.screens.user.profile.clients.ProfileRelationClient
import com.example.bbltripplanner.screens.user.profile.clients.UserClient
import com.example.bbltripplanner.screens.user.profile.repositories.GetProfileRepository
import com.example.bbltripplanner.screens.user.profile.repositories.ProfileRelationRepository
import com.example.bbltripplanner.screens.user.profile.repositoryImpl.GetProfileNetwork
import com.example.bbltripplanner.screens.user.profile.repositoryImpl.ProfileRelationNetwork
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileRelationUsecase
import com.example.bbltripplanner.screens.user.profile.usecases.ProfileUseCase
import com.example.bbltripplanner.screens.user.profile.viewModels.BlockedUsersViewModel
import com.example.bbltripplanner.screens.user.profile.viewModels.EditProfileViewModel
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileFollowersViewModel
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileFollowingViewModel
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileViewModel
import com.example.bbltripplanner.screens.userTrip.clients.ExpenseClient
import com.example.bbltripplanner.screens.userTrip.clients.ItineraryClient
import com.example.bbltripplanner.screens.userTrip.clients.LocationSearchClient
import com.example.bbltripplanner.screens.userTrip.clients.PostingClient
import com.example.bbltripplanner.screens.userTrip.clients.TripGalleryClient
import com.example.bbltripplanner.screens.userTrip.clients.UserTripDetailClient
import com.example.bbltripplanner.screens.userTrip.repositories.ExpenseRepository
import com.example.bbltripplanner.screens.userTrip.repositories.ItineraryRepository
import com.example.bbltripplanner.screens.userTrip.repositories.LocationSearchRepository
import com.example.bbltripplanner.screens.userTrip.repositories.PostingRepository
import com.example.bbltripplanner.screens.userTrip.repositories.TripGalleryRepository
import com.example.bbltripplanner.screens.userTrip.repositories.UserTripDetailRepository
import com.example.bbltripplanner.screens.userTrip.repositoryImpl.ExpenseNetwork
import com.example.bbltripplanner.screens.userTrip.repositoryImpl.ItineraryNetwork
import com.example.bbltripplanner.screens.userTrip.repositoryImpl.LocationSearchNetwork
import com.example.bbltripplanner.screens.userTrip.repositoryImpl.PostingNetwork
import com.example.bbltripplanner.screens.userTrip.repositoryImpl.TripGalleryRepositoryImpl
import com.example.bbltripplanner.screens.userTrip.repositoryImpl.UseTripDetailNetwork
import com.example.bbltripplanner.screens.userTrip.usecases.ExpenseUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.ItineraryUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.LocationSearchUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.PostingUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.TripGalleryUseCase
import com.example.bbltripplanner.screens.userTrip.usecases.UserTripDetailUseCase
import com.example.bbltripplanner.screens.userTrip.viewModels.ExpenseViewModel
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryDetailViewModel
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryMapViewModel
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryViewModel
import com.example.bbltripplanner.screens.userTrip.viewModels.PostingInitViewModel
import com.example.bbltripplanner.screens.userTrip.viewModels.TripGalleryViewModel
import com.example.bbltripplanner.screens.userTrip.viewModels.TripGroupViewModel
import com.example.bbltripplanner.screens.userTrip.viewModels.UserTripDetailViewModel
import com.example.bbltripplanner.screens.vault.clients.VaultClient
import com.example.bbltripplanner.screens.vault.repositories.VaultRepository
import com.example.bbltripplanner.screens.vault.repositoryImpl.VaultNetwork
import com.example.bbltripplanner.screens.vault.usecases.VaultUseCase
import com.example.bbltripplanner.screens.vault.viewModels.UserTripsViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // infra
    single<EncryptedPreferenceManager> { EncryptedPreferenceManager(androidContext()) }
    single<PreferenceManager> { PreferenceManager(androidContext(), get()) }
    single<Gson> { Gson() }

    // main
    viewModel { MainActivityViewModel(get(), get()) }

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "trip_planner_db"
        ).build()
    }
    single { get<AppDatabase>().tripPhotoDao() }

    // User
    single<UserClient> { Network.createWithAuth(UserClient::class.java, get(), get(), androidContext()) }
    single<GetProfileRepository> { GetProfileNetwork(get()) }
    single<ProfileUseCase> { ProfileUseCase(get()) }
    viewModel { (userId: String?) -> ProfileViewModel(userId, get(), get(), get()) }
    viewModel { MyAccountViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get()) }
    viewModel { BlockedUsersViewModel(get()) }

    // Home & Listing
    single<HomeCxeLayoutRepository> { HomeCxeLayoutNetwork(get()) }
    single<HomeCxeUseCase> { HomeCxeUseCase(get()) }
    single<HomeCxeClient> { Network.createWithAuth(HomeCxeClient::class.java, get(), get(), androidContext()) }
    viewModel { HomeExperienceViewModel(get(), get(),get()) }

    // Posting & Trips
    single<PostingClient> { Network.createWithAuth(PostingClient::class.java, get(), get(), androidContext()) }
    single<PostingRepository> { PostingNetwork(get()) }
    single<PostingUseCase> { PostingUseCase(get()) }
    single<UserTripDetailUseCase> { UserTripDetailUseCase(get()) }
    single<UserTripDetailRepository> { UseTripDetailNetwork(get()) }
    single<UserTripDetailClient> { Network.createWithAuth(UserTripDetailClient::class.java, get(), get(), androidContext()) }
    viewModel { (tripId: String?) -> PostingInitViewModel(tripId, get(), get(), get(), get(), get()) }
    viewModel { (tripId: String?) -> UserTripDetailViewModel(tripId, get(), get()) }
    viewModel { (tripId: String) -> TripGroupViewModel(tripId, get(), get(), get()) }

    // Itinerary
    single<ItineraryClient> { Network.createWithAuth(ItineraryClient::class.java, get(), get(), androidContext()) }
    single<ItineraryRepository> { ItineraryNetwork(get()) }
    single<ItineraryUseCase> { ItineraryUseCase(get()) }
    viewModel { (tripId: String?) ->
        ItineraryViewModel(tripId, get())
    }
    viewModel { ItineraryDetailViewModel(get()) }
    viewModel { (itineraryId: String?) ->
        ItineraryMapViewModel(itineraryId, get(), get())
    }

    // Expense
    single<ExpenseClient> { Network.createWithAuth(ExpenseClient::class.java, get(), get(), androidContext()) }
    single<ExpenseRepository> { ExpenseNetwork(get()) }
    single<ExpenseUseCase> { ExpenseUseCase(get()) }
    viewModel { (tripId: String?) -> ExpenseViewModel(tripId, get(), get()) }

    // Trip Gallery
    single<TripGalleryClient> { Network.createWithAuth(TripGalleryClient::class.java, get(), get(), androidContext()) }
    single<TripGalleryRepository> { TripGalleryRepositoryImpl(get(), get()) }
    single<TripGalleryUseCase> { TripGalleryUseCase(get()) }
    viewModel { (tripId: String?) -> TripGalleryViewModel(tripId, get()) }

    // User Auth
    single<UserAuthRepository> { UserAuthNetwork(get()) }
    single<UserAuthUseCase> { UserAuthUseCase(get()) }
    single<AuthPreferencesUseCase> { AuthPreferencesUseCase(get(), get()) }
    single<UserAuthClient> { Network.create(UserAuthClient::class.java, androidContext()) }
    viewModel { OTPAuthViewModel(get(), get(),get()) }
    viewModel { UserLoginAuthViewModel(get(), get(), get()) }
    viewModel { UseRegistrationViewModel(get()) }
    viewModel { ForgotPasswordAuthViewModel(get()) }
    viewModel { PasswordResetVieModel(get(), get()) }

    // Profile Relations
    single<ProfileRelationRepository> { ProfileRelationNetwork(get()) }
    single<ProfileRelationUsecase> { ProfileRelationUsecase(get()) }
    single<ProfileRelationClient> { Network.createWithAuth(ProfileRelationClient::class.java, get(), get(), androidContext()) }
    viewModel { (userId: String?) ->
        ProfileFollowersViewModel(get(), get(), userId)
    }
    viewModel { (userId: String?) ->
        ProfileFollowingViewModel(get(), get(), userId)
    }

    // Places Search
    single<LocationSearchClient> { Network.createLocationSearch(LocationSearchClient::class.java, androidContext()) }
    single<LocationSearchUseCase> { LocationSearchUseCase(get()) }
    single<LocationSearchRepository> { LocationSearchNetwork(get()) }

    // notification
    single<NotificationClient> { Network.createWithAuth(NotificationClient::class.java, get(), get(), androidContext()) }
    single<NotificationRepository> { NotificationNetwork(get()) }
    single<NotificationUseCase> { NotificationUseCase(get()) }
    viewModel {
        NotificationViewModel(get())
    }

    // Account
    viewModel { HelpSupportViewModel() }
    viewModel { UserSettingsViewModel(get(), get()) }

    // Vault
    single<VaultUseCase> { VaultUseCase(get()) }
    single<VaultRepository> { VaultNetwork(get()) }
    single<VaultClient> { Network.createWithAuth(VaultClient::class.java, get(), get(), androidContext()) }
    viewModel {
        UserTripsViewModel(get())
    }
}
