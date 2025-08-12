package com.example.bbltripplanner.screens.home.usecases

import com.example.bbltripplanner.screens.home.entities.HomeCxeResponse
import com.example.bbltripplanner.screens.home.repositories.HomeCxeLayoutRepository

class HomeCxeUseCase(
    private val homeCxeLayoutRepository: HomeCxeLayoutRepository
) {
    suspend fun getHomeCxeResponse(): HomeCxeResponse {
        return homeCxeLayoutRepository.getHomeCxeLayout()
    }
}