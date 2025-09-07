package com.example.bbltripplanner.common

import com.google.gson.annotations.SerializedName

object Constants {
    const val EMPTY_STRING =  ""
    const val PROFILE_DETAILS = "profile_details"
    const val NOTIFICATIONS = "notifications"
    const val SETTINGS = "settings"
    const val HELP_SUPPORT = "help_support"
    const val LOGOUT = "logout"
    const val DEFAULT_USER = "Traveller"
    const val APP_NAME = "TripPlanner"
    const val DEFAULT_ERROR = "default_error"
    const val TRIP_PLANNER_LOG_KEY = "TRIP_PLANNER_CURL"

    object HTTPHeaders {
        const val AUTHORIZATION_BEARER = "Bearer"
        const val AUTHORIZATION = "Authorization"
    }

    enum class Theme {
        @SerializedName("dark") DARK,
        @SerializedName("light") LIGHT
    }

    object NavigationScreen {
        // Home & Listing
        const val HOME_SCREEN = "home_screen"

        // general
        const val VAULT_SCREEN = "vault_screen"
        const val BUZZ_SCREEN = "buzz_screen"

        // Trip & Posting
        const val USER_TRIP_DETAIL_SCREEN = "user_trip_detail_screen"
        const val ADD_SCREEN = "add_screen"

        // User Screens
        const val PROFILE_SCREEN = "profile_screen"
        const val ACCOUNT_SCREEN = "my_account_screen"
        const val USER_SCREEN_GRAPH = "user_screen_graph"

        // Authentication
        const val AUTHENTICATION_FORM_SCREEN = "authentication_form_screen"
        const val RESET_PASSWORD_SCREEN = "reset_password_screen"
        const val OTP_VERIFICATION_SCREEN = "otp_verification_screen"
        const val FORGOT_PASSWORD_SCREEN = "forgot_password_Screen"
        const val AUTH_GRAPH = "auth_graph"
    }

    object NavigationArgs {
        const val TRIP_ID = "trip_id"
        const val USER_EMAIL = "user_email"
        const val ORIGIN = "origin"
        const val USER_ID = "user_id"
    }

    object Origin {
        const val REGISTRATION = "registration"
        const val FORGOT_PASSWORD  = "forgot_password"
    }

    object BottomNavigationItem {
        const val HOME = "Home"
        const val VAULT = "Vault"
        const val ADD = "Add"
        const val BUZZ = "Buzz"
        const val PROFILE = "Profile"
    }

    object TripDetailScreen {
        const val GENERAL = "General"
        const val ATTACHMENTS = "Attachments"
        const val ITINERARY = "Itinerary"
        const val EXPENSES = "Expenses"
        const val GROUP = "Group"
    }
}