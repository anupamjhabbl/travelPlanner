package com.example.bbltripplanner.common.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import com.example.bbltripplanner.common.Constants
import androidx.core.net.toUri

fun Context.openDeeplink(deeplink: String?) {
    if (deeplink?.isNotEmpty() == true) {
        val intent = Intent(Intent.ACTION_VIEW, deeplink.toUri())
        if (intent.resolveActivity(packageManager) != null) {
            this.startActivity(intent)
        }
    }
}

fun Context.shareDeepLinkOfTrip(message: String, tripId: String?) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Check out this trip!")
        putExtra(Intent.EXTRA_TEXT, "$message ${getDeeplinkUrl(tripId)}")
    }
    startActivity(Intent.createChooser(shareIntent, "Share deep link via"))
}

fun getDeeplinkUrl(tripId: String?): String {
    return if (tripId.isNullOrEmpty()) {
        Constants.TRIP_PLANNER_DEEPLINK
    } else {
        "${Constants.TRIP_PLANNER_DEEPLINK}/$tripId"
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    if (connectivityManager != null) {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
    return false
}