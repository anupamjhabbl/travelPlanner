package com.example.bbltripplanner.common.infra

import android.content.Context
import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.auth.usecases.UserAuthUseCase

object Network {
    fun <T> create(
        service: Class<T>,
        context: Context
    ): T {
        val networkModule = NetworkModule()
        return networkModule.provideRetrofitClient(context).create(service)
    }

    fun <T> createWithAuth(
        service: Class<T>,
        authPreferencesUseCase: AuthPreferencesUseCase,
        userAuthUseCase: UserAuthUseCase,
        context: Context
    ): T {
        val networkModule = NetworkModule()
        return networkModule.provideRetrofitClientWithAuth(
            authPreferencesUseCase,
            userAuthUseCase,
            context
        ).create(service)
    }
}