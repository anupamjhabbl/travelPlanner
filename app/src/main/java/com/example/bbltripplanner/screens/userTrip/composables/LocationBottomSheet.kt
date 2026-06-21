package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun LocationBottomSheet (
    locationList: List<Location>,
    searchQuery: String,
    isLocationLoading: Boolean,
    isLocationError: Boolean = false,
    errorMessage: String? = null,
    onQueryChanged: (query: String) -> Unit,
    updateLocation: (user: Location) -> Unit
) {
    Column(
        modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 0.dp, 16.dp, 16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { onQueryChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                ComposeTextView.TextView(
                    text = stringResource(R.string.search_by_name),
                    fontSize = 15.sp,
                    textColor = LocalCustomColors.current.hintTextColor
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LocalCustomColors.current.defaultImageCardColor.copy(alpha = 0.2f),
                unfocusedContainerColor = LocalCustomColors.current.defaultImageCardColor.copy(alpha = 0.1f),
                focusedBorderColor = LocalCustomColors.current.secondaryBackground,
                unfocusedBorderColor = LocalCustomColors.current.defaultImageCardColor,
                errorBorderColor = LocalCustomColors.current.error,
                disabledBorderColor = LocalCustomColors.current.fadedBackground
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLocationLoading) {
            Box(
                modifier = Modifier.height(150.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = LocalCustomColors.current.fadedBackground,
                    strokeWidth = 3.dp
                )
            }
        } else if (isLocationError) {
            Column(
                modifier = Modifier.height(150.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ComposeTextView.TextView(
                    text = errorMessage ?: "Failed to load locations",
                    fontSize = 16.sp,
                    textColor = LocalCustomColors.current.textColor,
                    textAlign = TextAlign.Center
                )
            }
        } else if (locationList.isEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ComposeTextView.TitleTextView(
                    text = if (searchQuery.isEmpty()) "Type to search locations..." else stringResource(R.string.no_result_found)
                )
            }
        } else {
            val filteredList = remember(locationList) {
                locationList.filter { it.displayName != null }
            }
            LazyColumn {
                itemsIndexed(filteredList) { index, location ->
                    val shape = when (index) {
                        0 -> RoundedCornerShape(12.dp, 12.dp, 2.dp, 2.dp)
                        filteredList.size - 1 -> RoundedCornerShape(2.dp, 2.dp, 12.dp, 12.dp)
                        else -> RoundedCornerShape(2.dp, 2.dp, 2.dp, 2.dp)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape)
                            .background(LocalCustomColors.current.defaultImageCardColor)
                            .clickable {
                                updateLocation(location)
                            }
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        ComposeTextView.TitleTextView(
                            text = location.displayName ?: "",
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }

                    Spacer(Modifier.height(1.dp))
                }
            }
        }
    }
}