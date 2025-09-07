package com.example.bbltripplanner.screens.posting.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.screens.home.entities.Location

@Composable
fun LocationBottomSheet (
    updateLocation: (user: Location) -> Unit
) {
    Column(
        modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 0.dp, 16.dp, 16.dp)
    ) {
        val locationList = listOf<Location>()

        OutlinedTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                ComposeTextView.TitleTextView(
                    "Search by name, email, contact",
                    fontSize = 16.sp
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(locationList) { index, location ->
                val shape = when (index) {
                    0 -> RoundedCornerShape(12.dp, 12.dp, 2.dp, 2.dp)
                    locationList.size - 1 -> RoundedCornerShape(2.dp, 2.dp, 12.dp, 12.dp)
                    else -> RoundedCornerShape(2.dp, 2.dp, 2.dp, 2.dp)
                }

                Box(
                    modifier = Modifier
                        .background(colorResource(R.color.bg_default_image), shape)
                        .height(50.dp)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable {
                            updateLocation(location)
                        }
                ) {
                    ComposeTextView.TitleTextView(
                        text = location.cityName,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

                Spacer(Modifier.height(1.dp))
            }
        }
    }
}