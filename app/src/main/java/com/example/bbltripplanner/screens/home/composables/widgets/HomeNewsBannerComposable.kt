package com.example.bbltripplanner.screens.home.composables.widgets

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget

@Composable
fun HomeNewsBannerComposable(widget: HomeCxeWidget.NewsBannerWidget) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 6.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardColors(colorResource(R.color.white), Color.Unspecified, colorResource(R.color.white), Color.Unspecified)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ComposeTextView.TitleTextView(
                    text = widget.data.header?.title ?: "",
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .background(colorResource(R.color.primary), RoundedCornerShape(8.dp))
                        .padding(12.dp, 4.dp)
                        .clickable(interactionSource = null, indication = null) {
                            openNewsPage(context)
                        }
                ) {
                    ComposeTextView.TitleTextView(
                        text = widget.data.actionHeader?.text ?: "",
                        textColor = colorResource(R.color.white),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ComposeTextView.TextView(
                text = widget.data.header?.subTitle ?: "",
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
        }
    }
}

private fun openNewsPage(context: Context) {
}
