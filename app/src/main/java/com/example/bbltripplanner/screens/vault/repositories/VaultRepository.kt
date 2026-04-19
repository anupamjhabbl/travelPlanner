package com.example.bbltripplanner.screens.vault.repositories

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import retrofit2.Response

interface VaultRepository {
    suspend fun getUserTrips(): Response<BaseResponse<List<TripData>>>
}
