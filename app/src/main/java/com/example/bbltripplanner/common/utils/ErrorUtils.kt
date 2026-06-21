package com.example.bbltripplanner.common.utils

import android.content.Context
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.entity.TripPlannerException
import retrofit2.HttpException
import java.io.IOException

object ErrorUtils {
    fun toErrorType(throwable: Throwable): String {
        return when {
            throwable is IOException -> Constants.ErrorType.NETWORK_ERROR
            throwable is HttpException && throwable.code() == 404 -> Constants.ErrorType.NOT_FOUND
            throwable is HttpException && throwable.code() == 403 -> Constants.ErrorType.NOT_AUTHORIZED
            throwable is TripPlannerException && throwable.errorCode in 500..599 -> Constants.ErrorType.SERVER_ERROR
            throwable is TripPlannerException -> throwable.message ?: Constants.ErrorType.SERVER_ERROR
            else -> Constants.ErrorType.SERVER_ERROR
        }
    }

    fun getErrorStrings(context: Context, errorType: String?): Pair<String, String> {
        return when (errorType) {
            Constants.ErrorType.NETWORK_ERROR -> Pair(
                context.getString(R.string.no_internet_connection),
                context.getString(R.string.no_internet_connection_subtitle)
            )
            Constants.ErrorType.SERVER_ERROR -> Pair(
                context.getString(R.string.server_error),
                context.getString(R.string.server_error_subtitle)
            )
            Constants.ErrorType.NOT_FOUND -> Pair(
                context.getString(R.string.nothing_to_show),
                context.getString(R.string.noting_to_show_subtitle)
            )
            Constants.ErrorType.NOT_AUTHORIZED -> Pair(
                context.getString(R.string.not_authorized_title),
                context.getString(R.string.not_authorized_subtitle)
            )
            else -> Pair(
                context.getString(R.string.server_error),
                errorType ?: context.getString(R.string.server_error_subtitle)
            )
        }
    }

    fun getMessage(context: Context, errorType: String?): String? {
        if (errorType == null) return null
        return when (errorType) {
            Constants.ErrorType.NETWORK_ERROR -> context.getString(R.string.no_internet_connection)
            Constants.ErrorType.SERVER_ERROR -> context.getString(R.string.something_went_wrong)
            Constants.ErrorType.NOT_FOUND -> context.getString(R.string.nothing_to_show)
            Constants.ErrorType.NOT_AUTHORIZED -> context.getString(R.string.not_authorized_subtitle)
            Constants.ErrorType.NO_LOCATION_AVAILABLE -> context.getString(R.string.no_location_availaible)
            else -> errorType
        }
    }
}
