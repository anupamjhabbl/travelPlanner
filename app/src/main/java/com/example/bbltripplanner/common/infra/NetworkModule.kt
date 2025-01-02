package com.example.bbltripplanner.common.infra

import com.example.bbltripplanner.common.entity.NetworkConfiguration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule(
    private val networkConfiguration: NetworkConfiguration
) {

    fun provideRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(networkConfiguration.baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideRetrofitClient(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}