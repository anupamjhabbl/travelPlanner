package com.example.bbltripplanner.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.bbltripplanner.common.Constants

fun Context.openDeeplink(deeplink: String?) {
    if (deeplink?.isNotEmpty() == true) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deeplink))
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