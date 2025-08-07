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

    object NavigationScreen {
        const val HOME_SCREEN = "home_screen"
        const val VAULT_SCREEN = "vault_screen"
        const val ADD_SCREEN = "add_screen"
        const val BUZZ_SCREEN = "buzz_screen"
        const val PROFILE_SCREEN = "profile_screen"
        const val ACCOUNT_SCREEN = "my_account_screen"
        const val USER_SCREEN_GRAPH = "user_screen_graph"
    }

    object BottomNavigationItem {
        const val HOME = "Home"
        const val VAULT = "Vault"
        const val ADD = "Add"
        const val BUZZ = "Buzz"
        const val PROFILE = "Profile"
    }
}