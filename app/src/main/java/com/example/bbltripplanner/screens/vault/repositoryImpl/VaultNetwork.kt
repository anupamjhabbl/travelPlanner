package com.example.bbltripplanner.screens.vault.repositoryImpl

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.vault.clients.VaultClient
import com.example.bbltripplanner.screens.vault.repositories.VaultRepository
import retrofit2.Response

class VaultNetwork(private val vaultClient: VaultClient) : VaultRepository {
    override suspend fun getUserTrips(): Response<BaseResponse<List<TripData>>> {
        return vaultClient.getUserTrips()
    }
}
