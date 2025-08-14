package com.example.bbltripplanner.screens.home.composables.widgets

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget

@Composable
fun HomeImageCarouselComposable(widget: HomeCxeWidget.ImageCarouselWidget) {
    Text("HomeImageCarouselComposable")
    Log.d("RESPONSEE", widget.toString())
}