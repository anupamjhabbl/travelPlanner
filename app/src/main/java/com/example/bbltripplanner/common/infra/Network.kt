package com.example.bbltripplanner.common.infra

object Network {
    fun <T> create(
        service: Class<T>
    ): T {
        val networkModule = NetworkModule()
        return networkModule.provideRetrofitClient().create(service)
    }
}