package com.example.bbltripplanner.common.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.window.layout.WindowMetricsCalculator

object WindowSizeUtils {
    fun getWindowWidth(context: Context): Int {
        val activity = context.findActivity()
        val windowMetrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(activity)
        return windowMetrics.bounds.width()
    }

}

fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    throw IllegalStateException("Context is not an Activity")
}