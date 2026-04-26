package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryActivity
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryDetailIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryDetailViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ItineraryDetailView(
    placeId: String?
) {
    val viewModel: ItineraryDetailViewModel = koinViewModel()
    val customColors = LocalCustomColors.current
    val placeDetailStatus by viewModel.placeDetailStatus.collectAsState()
    val errorMessage = stringResource(R.string.generic_error)
    val scope = rememberCoroutineScope()

    LaunchedEffect(placeId) {
        placeId?.let {
            viewModel.processEvent(ItineraryDetailIntent.ViewEvent.FetchPlaceDetail(it))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
    ) {
        if (placeDetailStatus.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = customColors.secondaryBackground)
            }
        } else if (placeDetailStatus.error != null) {
            ComposeViewUtils.FullScreenErrorComposable(Pair(errorMessage, placeDetailStatus.error ?: ""))
        } else if (placeDetailStatus.data == null) {
            ComposeViewUtils.FullScreenErrorComposable(Pair(stringResource(R.string.no_place_in_itinerary),
                stringResource(R.string.no_such_place)
            ))
        } else {
            val place = placeDetailStatus.data
            place?.let {
                Column (modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            PlaceHeader(place.imageUrl, place.placeName, place.address)
                        }

                        item {
                            Column(
                                modifier = Modifier.padding(dimensionResource(id = R.dimen.module_16))
                            ) {
                                ComposeTextView.TitleTextView(
                                    text = stringResource(R.string.about),
                                    fontSize = 18.sp,
                                    textColor = customColors.secondaryBackground
                                )

                                ComposeTextView.TextView(
                                    text = place.description,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.module_24)))

                                ComposeTextView.TitleTextView(
                                    text = stringResource(R.string.activities),
                                    fontSize = 18.sp,
                                    textColor = customColors.secondaryBackground
                                )
                            }
                        }

                        items(place.activityList) { activity ->
                            ActivityItem(activity)

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.module_16)))
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .align(Alignment.TopCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.15f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        ComposeButtonView.PrimaryButtonView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(id = R.dimen.module_16)),
                            backgroundColor = customColors.secondaryBackground,
                            contentColor = customColors.primaryBackground,
                            text = stringResource(R.string.add_new_activity),
                            fontSize = 16.sp,
                            onClick = {
                                scope.launch {
                                    placeId?.let {
                                        CommonNavigationChannel.navigateTo(
                                            NavigationAction.Navigate(
                                                AppNavigationScreen.AddActivityScreen.createRoute(
                                                    it
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceHeader(
    imageUrl: String?,
    name: String,
    address: String?
) {
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        ComposeImageView.ImageViewWithUrl(
            imageURI = imageUrl ?: "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.module_16))
                .size(dimensionResource(id = R.dimen.module_36))
                .background(Color.White.copy(alpha = 0.3f), CircleShape)
                .clickable {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Back",
                modifier = Modifier.size(dimensionResource(id = R.dimen.module_20)),
                tint = Color.White
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(customColors.primaryBackground)
                .padding(dimensionResource(id = R.dimen.module_20))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    ComposeTextView.TitleTextView(
                        text = name,
                        fontSize = 24.sp,
                        textColor = customColors.titleTextColor
                    )
                    address?.let {
                        ComposeTextView.TextView(
                            text = it,
                            fontSize = 12.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_attachment),
                        contentDescription = null,
                        tint = customColors.secondaryBackground,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    ComposeTextView.TextView(
                        text = stringResource(R.string.attachments),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textColor = customColors.secondaryBackground
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityItem(activity: ItineraryActivity) {
    val customColors = LocalCustomColors.current

    Row(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.module_16))
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.module_16)))
            .background(customColors.deepPurpleGlow)
            .padding(dimensionResource(id = R.dimen.module_16))
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ComposeTextView.TextView(
                    text = activity.activityName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textColor = customColors.titleTextColor
                )
                
                ComposeTextView.TextView(
                    text = "${activity.startTime} - ${activity.endTime}",
                    fontSize = 12.sp,
                    textColor = customColors.secondaryBackground,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.module_8)))
            ComposeTextView.TextView(
                text = activity.description,
                fontSize = 14.sp,
                textColor = customColors.textColor
            )
        }
    }
}
