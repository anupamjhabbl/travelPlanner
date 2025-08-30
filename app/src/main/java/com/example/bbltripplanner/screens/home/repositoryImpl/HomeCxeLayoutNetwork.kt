package com.example.bbltripplanner.screens.home.repositoryImpl

import com.example.bbltripplanner.common.entity.ApiFailureException
import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.utils.JsonResponseUtils
import com.example.bbltripplanner.screens.home.clients.HomeCxeClient
import com.example.bbltripplanner.screens.home.entities.BundleWidgetItem
import com.example.bbltripplanner.screens.home.entities.HomeCxeResponse
import com.example.bbltripplanner.screens.home.entities.TravelThreadsWidgetItem
import com.example.bbltripplanner.screens.home.entities.UserTripWidgetItem
import com.example.bbltripplanner.screens.home.repositories.HomeCxeLayoutRepository
import com.google.gson.JsonParseException

class HomeCxeLayoutNetwork(
    private val homeCxeClient: HomeCxeClient
): HomeCxeLayoutRepository {
    override suspend fun getHomeCxeLayout(): HomeCxeResponse {
        return processResponse(homeCxeClient.getHomeCxeData())
    }

    override suspend fun getTravelThreadBundleData(bundleUri: String): List<TravelThreadsWidgetItem> {
        return processResponse(homeCxeClient.getTravelThreadBundleData(bundleUri))
    }

    override suspend fun getUserTripBundleData(bundleUri: String): List<UserTripWidgetItem> {
        return processResponse(homeCxeClient.getUserTripBundleData(bundleUri))
    }

    override suspend fun getBundleWidgetData(bundleUri: String): List<BundleWidgetItem> {
        return processResponse(homeCxeClient.getBundleWidgetData(bundleUri))
    }

    private fun <T> processResponse(homeCxeData: BaseResponse<T>): T {
         if (homeCxeData.isSuccess && homeCxeData.data != null) {
             return homeCxeData.data
        } else {
            if (homeCxeData.statusCode == JsonResponseUtils.HTTP_SUCCESS_RESPONSE_CODE) {
                throw JsonParseException("Error during parsing")
            } else{
                throw ApiFailureException("Api Failure happened with status code ${homeCxeData.statusCode}")
            }
        }
    }
}