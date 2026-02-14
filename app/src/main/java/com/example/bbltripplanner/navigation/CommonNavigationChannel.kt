package com.example.bbltripplanner.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object CommonNavigationChannel {
    private val _navigationChannel: Channel<NavigationAction> = Channel(2, BufferOverflow.DROP_OLDEST)
    val navigationChannel: Flow<NavigationAction> = _navigationChannel.receiveAsFlow()

    suspend fun navigateTo(navigationScreen: NavigationAction) {
        _navigationChannel.send(navigationScreen)
    }

    fun navigateToSynchronous(navigationScreen: NavigationAction) {
        _navigationChannel.trySend(navigationScreen)
    }
}