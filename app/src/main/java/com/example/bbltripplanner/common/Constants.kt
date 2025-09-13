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
    const val TRIP_PAGE = "TRIP_PAGE"
    const val BUZZ_PAGE = "BUZZ_PAGE"
    const val REVIEW_PAGES = "REVIEW_PAGES"
    const val FAVOURITES  = "FAVOURITES"
    const val CONTACTS = "CONTACTS"
    const val APP_NAME_URI = "tripplanner"

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
        const val NOTIFICATION_SCREEN = "notifictaion_screen"

        // Trip & Posting
        const val USER_TRIP_DETAIL_SCREEN = "user_trip_detail_screen"
        const val ADD_SCREEN = "add_screen"

        // User Screens
        const val PROFILE_SCREEN = "profile_screen"
        const val ACCOUNT_SCREEN = "my_account_screen"
        const val EDIT_PROFILE_SCREEN = "edit_profile_screen"
        const val USER_SCREEN_GRAPH = "user_screen_graph"
        const val USER_SETTING_SCREEN = "user_setting_screen"
        const val HELP_SUPPORT_SCREEN = "help_support_screen"
        const val PROFILE_SOCIAL_SCREEN = "contacts_screen"

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
        const val PAGE_ID = "page_id"
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