package com.example.bbltripplanner.screens.home.composables.widgets

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget

@Composable
fun HomeNewsBannerComposable(widget: HomeCxeWidget.NewsBannerWidget) {
    Text("HomeNewsBannerComposable")
    Log.d("RESPONSEE", widget.toString())
}