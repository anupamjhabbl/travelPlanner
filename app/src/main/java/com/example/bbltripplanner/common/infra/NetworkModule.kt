package com.example.bbltripplanner.common.infra

import android.content.Context
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.auth.usecases.UserAuthUseCase
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule {
    fun provideRetrofitClient(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(InfraProvider.getNetworkConfiguration.baseURL)
            .client(provideOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create(InfraProvider.gson))
            .build()
    }

    fun provideLocationSearchClient(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(InfraProvider.getLocationSearchNetworkConfiguration.baseURL)
            .client(provideOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create(InfraProvider.gson))
            .build()
    }

    fun provideRetrofitClientWithAuth(authPreferencesUseCase: AuthPreferencesUseCase, userAuthUseCase: UserAuthUseCase, context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(InfraProvider.getNetworkConfiguration.baseURL)
            .client(provideAuthOkHTTPClient(authPreferencesUseCase, userAuthUseCase, context))
            .addConverterFactory(GsonConverterFactory.create(InfraProvider.gson))
            .build()
    }

    private fun provideAuthOkHTTPClient(authPreferencesUseCase: AuthPreferencesUseCase, userAuthUseCase: UserAuthUseCase, context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(authPreferencesUseCase))
            .addInterceptor(HeaderInterceptor(context))
            .addInterceptor(LoggerInterceptor())
            .authenticator(TokenAuthenticator(authPreferencesUseCase, userAuthUseCase))
            .build()
    }

    private fun provideOkHttpClient(context: Context): OkHttpClient {
         return OkHttpClient.Builder()
             .addInterceptor(HeaderInterceptor(context))
             .addInterceptor(LoggerInterceptor())
             .build()
    }

}