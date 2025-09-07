package com.example.bbltripplanner.common.infra

import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.auth.usecases.UserAuthUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule {
    fun provideRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(InfraProvider.getNetworkConfiguration.baseURL)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(InfraProvider.gson))
            .build()
    }

    fun provideRetrofitClientWithAuth(authPreferencesUseCase: AuthPreferencesUseCase, userAuthUseCase: UserAuthUseCase): Retrofit {
        return Retrofit.Builder()
            .baseUrl(InfraProvider.getNetworkConfiguration.baseURL)
            .client(provideAuthOkHTTPClient(authPreferencesUseCase, userAuthUseCase))
            .addConverterFactory(GsonConverterFactory.create(InfraProvider.gson))
            .build()
    }

    private fun provideAuthOkHTTPClient(authPreferencesUseCase: AuthPreferencesUseCase, userAuthUseCase: UserAuthUseCase): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(authPreferencesUseCase))
            .addInterceptor(LoggerInterceptor())
            .authenticator(TokenAuthenticator(authPreferencesUseCase, userAuthUseCase))
            .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
         return OkHttpClient.Builder()
             .addInterceptor(LoggerInterceptor())
             .build()
    }

}