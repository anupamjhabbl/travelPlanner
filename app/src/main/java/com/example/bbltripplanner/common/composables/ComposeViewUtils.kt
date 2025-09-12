package com.example.bbltripplanner.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.ui.theme.LocalCustomColors

object ComposeViewUtils {
    @Composable
    fun Loading(
        modifier: Modifier = Modifier,
        color: Color = LocalCustomColors.current.secondaryBackground,
        strokeWidth: Dp = 3.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = modifier,
                color = color,
                strokeWidth = strokeWidth
            )
        }
    }

    @Composable
    fun FullScreenErrorComposable(
        errorStrings: Pair<String, String>,
        isActionButton: Boolean,
        onActionButtonClick: () -> Unit = {},
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ComposeImageView.ImageViewWitDrawableId(
                imageId = R.drawable.ic_vault_filled,
                contentDescription = "Error",
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ComposeTextView.TitleTextView(
                text = errorStrings.first,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            ComposeTextView.TextView(
                text = errorStrings.second,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isActionButton) {
                Button(
                    onClick = onActionButtonClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(LocalCustomColors.current.secondaryBackground)
                ) {
                    ComposeTextView.TitleTextView(
                        text = "Retry",
                        textColor = LocalCustomColors.current.primaryBackground,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    @Composable
    fun FullScreenLoading() {
        Loading(
            modifier = Modifier.size(40.dp)
        )
    }
}