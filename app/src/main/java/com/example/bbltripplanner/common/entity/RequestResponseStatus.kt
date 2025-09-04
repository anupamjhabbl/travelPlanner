package com.example.bbltripplanner.common.entity

data class RequestResponseStatus<T>(
    val isLoading: Boolean = false,
    val error: ApiFailureException? = null,
    val data: T? = null
)