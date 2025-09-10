package com.example.bbltripplanner.common.entity

data class RequestResponseStatus<T>(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: T? = null
)

sealed class RequestStatus<out T> {
    data object Idle : RequestStatus<Nothing>()
    data object Loading : RequestStatus<Nothing>()
    data class Success<T>(val data: T) : RequestStatus<T>()
    data class Error(val message: String?) : RequestStatus<Nothing>()
}
