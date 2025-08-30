package com.example.bbltripplanner.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openDeeplink(deeplink: String?) {
    if (deeplink?.isNotEmpty() == true) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deeplink))
        if (intent.resolveActivity(packageManager) != null) {
            this.startActivity(intent)
        }
    }
}