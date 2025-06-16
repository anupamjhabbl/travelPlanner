package com.example.bbltripplanner.common.entity

data class BaseResponse<T>(
    val data: T?,
    val statusCode: Int,
    val isSuccess: Boolean
)
