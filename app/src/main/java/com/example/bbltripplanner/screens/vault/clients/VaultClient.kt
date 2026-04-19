package com.example.bbltripplanner.screens.vault.clients

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import retrofit2.Response
import retrofit2.http.GET

interface VaultClient {
    @GET("trips/getTrips")
    suspend fun getUserTrips(): Response<BaseResponse<List<TripData>>>
}
