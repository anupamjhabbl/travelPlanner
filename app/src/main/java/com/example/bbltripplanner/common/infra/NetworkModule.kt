package com.example.bbltripplanner.common.infra

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule {

    fun provideRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(InfraProvider.getNetworkConfiguration.baseURL)
            .addConverterFactory(GsonConverterFactory.create(InfraProvider.gson))
            .build()
    }

    fun provideRetrofitClient(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(InfraProvider.gson))
            .build()
    }
}