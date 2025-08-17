package com.example.bbltripplanner.screens.home.composables.widgets

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget

@Composable
fun HomeUserTripBundleWidgetComposable(widget: HomeCxeWidget.UserTripBundleWidget) {
    Text("HomeUserTripBundleComposable")
    Log.d("RESPONSEE", widget.toString())
}
