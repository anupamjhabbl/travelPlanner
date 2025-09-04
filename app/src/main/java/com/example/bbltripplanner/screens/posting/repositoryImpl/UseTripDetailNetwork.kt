package com.example.bbltripplanner.screens.posting.repositoryImpl

import com.example.bbltripplanner.common.entity.ApiFailureException
import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.utils.JsonResponseUtils
import com.example.bbltripplanner.screens.posting.clients.UserTripDetailClient
import com.example.bbltripplanner.screens.posting.entity.TripData
import com.example.bbltripplanner.screens.posting.repositories.UserTripDetailRepository
import com.google.gson.JsonParseException

class UseTripDetailNetwork(
    private val userTripDetailClient: UserTripDetailClient
): UserTripDetailRepository {
    override suspend fun getUserTripDetail(tripId: String): TripData {
        return processResponse(userTripDetailClient.getUserTripDetail(tripId))
    }

    private fun processResponse(userTripDetail: BaseResponse<TripData>): TripData {
        if (userTripDetail.isSuccess && userTripDetail.data != null) {
            return userTripDetail.data
        } else {
            if (userTripDetail.statusCode == JsonResponseUtils.HTTP_SUCCESS_RESPONSE_CODE) {
                throw JsonParseException("Error during parsing")
            } else{
                throw ApiFailureException("Api Failure happened with status code ${userTripDetail.statusCode}")
            }
        }
    }

}