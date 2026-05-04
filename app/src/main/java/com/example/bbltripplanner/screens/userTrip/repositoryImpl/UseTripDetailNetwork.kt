package com.example.bbltripplanner.screens.userTrip.repositoryImpl

import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.entity.ApiFailureException
import com.example.bbltripplanner.common.entity.BaseResponse
import com.example.bbltripplanner.common.utils.JsonResponseUtils
import com.example.bbltripplanner.screens.userTrip.clients.UserTripDetailClient
import com.example.bbltripplanner.screens.userTrip.entity.TripData
import com.example.bbltripplanner.screens.userTrip.entity.TripMember
import com.example.bbltripplanner.screens.userTrip.repositories.UserTripDetailRepository
import com.google.gson.JsonParseException
import kotlin.text.ifEmpty

class UseTripDetailNetwork(
    private val userTripDetailClient: UserTripDetailClient
): UserTripDetailRepository {
    override suspend fun getUserTripDetail(tripId: String): TripData {
        return processResponse(userTripDetailClient.getUserTripDetail(tripId))
    }

    override suspend fun acceptInvitation(
        tripId: String
    ): Boolean {
        return processAcceptInvitationResponse(userTripDetailClient.acceptInvitation(tripId))
    }

    override suspend fun getTripMembers(tripId: String): List<TripMember> {
        return processTripMembersResponse(userTripDetailClient.getTripMembers(tripId))
    }

    override suspend fun addTripMember(tripId: String, userId: String): Boolean {
        return processAcceptInvitationResponse(userTripDetailClient.addTripMember(tripId, userId))
    }

    private fun processTripMembersResponse(response: BaseResponse<List<TripMember>>): List<TripMember> {
        if (response.isSuccess && response.data != null) {
            return response.data
        } else {
            if (response.statusCode == JsonResponseUtils.HTTP_SUCCESS_RESPONSE_CODE) {
                throw JsonParseException("Error during parsing")
            } else {
                throw ApiFailureException(response.message?.ifEmpty { Constants.DEFAULT_ERROR_MESSAGE })
            }
        }
    }

    private fun processAcceptInvitationResponse(acceptInvitation: BaseResponse<Boolean>): Boolean {
        if (acceptInvitation.isSuccess && acceptInvitation.data != null) {
            return acceptInvitation.data
        } else {
            if (acceptInvitation.statusCode == JsonResponseUtils.HTTP_SUCCESS_RESPONSE_CODE) {
                throw JsonParseException("Error during parsing")
            } else {
                throw ApiFailureException(acceptInvitation.message?.ifEmpty { Constants.DEFAULT_ERROR_MESSAGE })
            }
        }
    }

    private fun processResponse(userTripDetail: BaseResponse<TripData>): TripData {
        if (userTripDetail.isSuccess && userTripDetail.data != null) {
            return userTripDetail.data
        } else {
            if (userTripDetail.statusCode == JsonResponseUtils.HTTP_SUCCESS_RESPONSE_CODE) {
                throw JsonParseException("Error during parsing")
            } else {
                throw ApiFailureException(userTripDetail.message?.ifEmpty { Constants.DEFAULT_ERROR_MESSAGE })
            }
        }
    }
}