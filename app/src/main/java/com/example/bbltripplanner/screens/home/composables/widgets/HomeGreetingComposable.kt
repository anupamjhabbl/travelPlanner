package com.example.bbltripplanner.screens.home.composables.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget

@Composable
fun HomeGreetingComposable(widget: HomeCxeWidget.GreetingWidget, userName: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
    ) {
        ComposeTextView.TitleTextView(
            text = String.format(stringResource(R.string.user_greeting_hi), userName ?: Constants.DEFAULT_USER),
        )

        ComposeTextView.TextView(
            text = widget.data.greetingText,
        )
    }
}