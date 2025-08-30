package com.example.bbltripplanner.screens.home.composables.widgets

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget

@Composable
fun HomeLocationFeedCtaComposable(widget: HomeCxeWidget.TopPicksByLocationCtaWidget) {
    val context = LocalContext.current

    ComposeButtonView.PrimaryButtonView (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp),
        text = widget.data.header?.title ?: "",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ) {
        openLocationFeedPage(context)
    }
}

private fun openLocationFeedPage(context: Context) {
}
