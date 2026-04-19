package com.example.bbltripplanner.screens.home.repositories

import com.example.bbltripplanner.screens.home.entities.BundleWidgetItem
import com.example.bbltripplanner.screens.home.entities.HomeCxeResponse
import com.example.bbltripplanner.screens.home.entities.TravelThreadsWidgetItem

interface HomeCxeLayoutRepository {
    suspend fun getHomeCxeLayout(): HomeCxeResponse
    suspend fun getTravelThreadBundleData(bundleUri: String): List<TravelThreadsWidgetItem>
    suspend fun getBundleWidgetData(bundleUri: String): List<BundleWidgetItem>
}