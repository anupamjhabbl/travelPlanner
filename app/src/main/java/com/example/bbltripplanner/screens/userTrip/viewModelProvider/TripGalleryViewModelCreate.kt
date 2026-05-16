package com.example.bbltripplanner.screens.userTrip.viewModelProvider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.screens.userTrip.viewModels.TripGalleryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun rememberTripGalleryViewModel(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry
): TripGalleryViewModel {
    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(AppNavigationScreen.TripGalleryNavEntry.route)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}