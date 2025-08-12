package com.example.bbltripplanner.screens.home.repositoryImpl

import com.example.bbltripplanner.common.entity.ApiFailureException
import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.utils.JsonResponseUtils
import com.example.bbltripplanner.screens.home.clients.HomeCxeClient
import com.example.bbltripplanner.screens.home.entities.HomeCxeResponse
import com.example.bbltripplanner.screens.home.repositories.HomeCxeLayoutRepository
import com.google.gson.JsonParseException

class HomeCxeLayoutNetwork(
    private val homeCxeClient: HomeCxeClient
): HomeCxeLayoutRepository {
    override suspend fun getHomeCxeLayout(): HomeCxeResponse {
        return processResponse(homeCxeClient.getHomeCxeData())
    }

    private fun processResponse(homeCxeData: BaseResponse<HomeCxeResponse>): HomeCxeResponse {
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