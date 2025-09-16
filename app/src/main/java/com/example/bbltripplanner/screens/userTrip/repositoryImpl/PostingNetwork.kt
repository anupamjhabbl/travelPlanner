package com.example.bbltripplanner.screens.userTrip.repositoryImpl

import com.example.bbltripplanner.common.entity.ApiFailureException
import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.utils.JsonResponseUtils
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.clients.PostingClient
import com.example.bbltripplanner.screens.userTrip.repositories.PostingRepository
import com.google.gson.JsonParseException

class PostingNetwork(
    private val postingClient: PostingClient
): PostingRepository {
    override suspend fun postTrip(tripData: TripData): TripData {
        return processResponse(postingClient.postTrip(tripData))
    }

    private fun processResponse(postTrip: BaseResponse<TripData>): TripData {
        if (postTrip.isSuccess && postTrip.data != null) {
            return postTrip.data
        } else {
            if (postTrip.statusCode == JsonResponseUtils.HTTP_SUCCESS_RESPONSE_CODE) {
                throw JsonParseException("Error during parsing")
            } else{
                throw ApiFailureException("Api Failure happened with status code ${postTrip.statusCode}")
            }
        }
    }
}