package com.example.bbltripplanner.screens.vault.usecases

import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.vault.repositories.VaultRepository

class VaultUseCase(
    private val vaultRepository: VaultRepository
) {
    suspend fun getUserTrips(): List<TripData>? {
        return BaseResponse.processResponse { vaultRepository.getUserTrips() }
    }
}
