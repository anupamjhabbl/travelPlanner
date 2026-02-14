package com.example.bbltripplanner.navigation

import androidx.navigation.NavOptionsBuilder

sealed interface NavigationAction {
    data class Navigate(
        val destination: String,
        val navOptions: (NavOptionsBuilder.() -> Unit)? = null
    ): NavigationAction

    data object NavigateUp: NavigationAction
}