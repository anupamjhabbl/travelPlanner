package com.example.bbltripplanner.common.entity

import com.google.gson.Gson
import retrofit2.Response

data class BaseResponse<T>(
    val data: T?,
    val statusCode: Int,
    val isSuccess: Boolean,
    val message: String? = null
) {
    companion object {
        suspend fun <T> processResponse(apiCall: suspend () -> Response<BaseResponse<T>>): T? {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                    ?: throw Exception("Response body is null")

                if (body.isSuccess) {
                    return body.data
                } else {
                    throw TripPlannerException(
                        errorCode = body.statusCode,
                        message = body.message ?: "Unknown error"
                    )
                }
            } else {
                val errorJson = response.errorBody()?.string()
                try {
                    val error = Gson().fromJson(errorJson, BaseResponse::class.java) as BaseResponse<*>
                    throw TripPlannerException( errorCode = error.statusCode, message = error.message ?: "" )
                } catch (e: Exception) {
                    if (e is TripPlannerException) {
                        throw e
                    } else {
                        throw Exception(response.message())
                    }
                }
            }
        }
    }
}





