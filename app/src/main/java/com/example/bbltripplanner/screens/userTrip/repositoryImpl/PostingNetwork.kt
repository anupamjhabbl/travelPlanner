package com.example.bbltripplanner.screens.userTrip.repositoryImpl

import com.example.bbltripplanner.common.entity.ApiFailureException
import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.utils.JsonResponseUtils
import com.example.bbltripplanner.screens.userTrip.clients.PostingClient
import com.example.bbltripplanner.screens.userTrip.entity.TripCreationResponse
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.TripDataRequestModel
import com.example.bbltripplanner.screens.userTrip.entity.toModel
import com.example.bbltripplanner.screens.userTrip.repositories.PostingRepository
import com.google.gson.JsonParseException

class PostingNetwork(
    private val postingClient: PostingClient
): PostingRepository {
    override suspend fun postTrip(tripData: TripData): TripCreationResponse {
        return processResponse(postingClient.postTrip(tripData.toModel()))
    }

    override suspend fun updateTrip(tripId: String, tripData: TripData): Boolean {
        return postingClient.updateTrip(tripId, tripData.toModel()).isSuccess
    }

    private fun processResponse(postTrip: BaseResponse<TripCreationResponse>): TripCreationResponse {
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

private fun TripData.toModel(): TripDataRequestModel {
    return TripDataRequestModel(
        tripId,
        tripName,
        whereTo?.toModel(),
        startDate,
        endDate,
        invitedMembers.map { it.id },
        visibility
    )
}
