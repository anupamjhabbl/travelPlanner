package com.example.bbltripplanner.common.infra

import com.example.bbltripplanner.screens.user.auth.usecases.AuthPreferencesUseCase
import com.example.bbltripplanner.screens.user.auth.usecases.UserAuthUseCase

object Network {
    fun <T> create(
        service: Class<T>
    ): T {
        val networkModule = NetworkModule()
        return networkModule.provideRetrofitClient().create(service)
    }

    fun <T> createWithAuth(
        service: Class<T>,
        authPreferencesUseCase: AuthPreferencesUseCase,
        userAuthUseCase: UserAuthUseCase
    ): T {
        val networkModule = NetworkModule()
        return networkModule.provideRetrofitClientWithAuth(
            authPreferencesUseCase,
            userAuthUseCase
        ).create(service)
    }
}