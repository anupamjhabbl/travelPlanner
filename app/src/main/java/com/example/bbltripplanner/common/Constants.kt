package com.example.bbltripplanner.common

import com.google.gson.annotations.SerializedName

object Constants {
    const val EMPTY_STRING =  ""
    const val PROFILE_DETAILS = "profile_details"
    const val NOTIFICATIONS = "notifications"
    const val SETTINGS = "settings"
    const val HELP_SUPPORT = "help_support"
    const val LOGOUT = "logout"

    enum class Theme {
        @SerializedName("dark") DARK,
        @SerializedName("light") LIGHT
    }
}