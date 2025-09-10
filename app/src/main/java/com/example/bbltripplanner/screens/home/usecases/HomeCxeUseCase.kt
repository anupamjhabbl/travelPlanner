package com.example.bbltripplanner.screens.home.usecases

import com.example.bbltripplanner.screens.home.entities.BundleWidgetItem
import com.example.bbltripplanner.screens.home.entities.HomeCxeResponse
import com.example.bbltripplanner.screens.home.entities.TravelThreadsWidgetItem
import com.example.bbltripplanner.screens.home.entities.UserTripWidgetItem
import com.example.bbltripplanner.screens.home.repositories.HomeCxeLayoutRepository

class HomeCxeUseCase(
    private val homeCxeLayoutRepository: HomeCxeLayoutRepository
) {
    suspend fun getHomeCxeResponse(): HomeCxeResponse {
        return homeCxeLayoutRepository.getHomeCxeLayout()
    }

    suspend fun getTravelThreadBundleData(bundleUri: String): List<TravelThreadsWidgetItem> {
        return homeCxeLayoutRepository.getTravelThreadBundleData(bundleUri)
    }

    suspend fun getUserTripBundleData(bundleUri: String): List<UserTripWidgetItem> {
        return homeCxeLayoutRepository.getUserTripBundleData(bundleUri)
    }

    suspend fun getBundleWidgetData(bundleUri: String): List<BundleWidgetItem> {
        return homeCxeLayoutRepository.getBundleWidgetData(bundleUri)
    }
}