package com.example.bbltripplanner.common.entity

data class BaseResponse<T>(
    val data: T?,
    val statusCode: Int,
    val isSuccess: Boolean,
    val errorMessage: String? = null
) {
    fun processResponse(): T {
        if (this.isSuccess && this.data != null) {
            return this.data
        } else {
            throw TripPlannerException(
                errorCode = this.statusCode,
                message = this.errorMessage ?: ""
            )
        }
    }
}

