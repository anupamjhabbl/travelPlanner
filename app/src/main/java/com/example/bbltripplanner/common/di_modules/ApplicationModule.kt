package com.example.bbltripplanner.common.di_modules

import com.example.bbltripplanner.common.infra.Network
import com.example.bbltripplanner.user.UserClient
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class ApplicationModule {
    @Single
    fun getUserClient(): UserClient {
        return Network.create(UserClient::class.java)
    }
}