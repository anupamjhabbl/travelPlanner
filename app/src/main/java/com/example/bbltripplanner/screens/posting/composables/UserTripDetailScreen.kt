package com.example.bbltripplanner.screens.posting.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils.FullScreenLoading
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.posting.entity.TripActionItem
import com.example.bbltripplanner.screens.posting.entity.TripActionResourceMapper
import com.example.bbltripplanner.screens.posting.entity.TripData
import com.example.bbltripplanner.screens.posting.viewModels.UserTripDetailIntent
import com.example.bbltripplanner.screens.posting.viewModels.UserTripDetailViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserTripDetailScreen(
    navController: NavController,
    tripId: String?
) {
    val tripActionList = TripActionResourceMapper.getTripActions()
    val viewModel: UserTripDetailViewModel = koinViewModel()
    val tripDataStatus = viewModel.userTripDetailFetchStatus.collectAsState()

    LaunchedEffect(Unit) {
        tripId?.let {
            viewModel.processEvent(UserTripDetailIntent.ViewEvent.FetchTripDetail(tripId))
        }
    }

    if (tripDataStatus.value.isLoading) {
        FullScreenLoading()
    } else if(tripDataStatus.value.data == null ||  tripDataStatus.value.error != null) {
        FullScreenError()
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            UserTripDetailToolbar(navController)

            Spacer(Modifier.height(30.dp))

            TripSummarySection(tripDataStatus.value.data)

            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                itemsIndexed(tripActionList) { index, item ->
                    Column {
                        ActionTile(
                            item
                        ) { key ->
                            takeAction(navController, key)
                        }

                        if (index != tripActionList.size - 1) {
                            Spacer(
                                Modifier
                                    .height(1.dp)
                                    .fillMaxWidth()
                                    .background(LocalCustomColors.current.defaultImageCardColor)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FullScreenError() {

}

@Composable
private fun TripSummarySection(
    userTripData: TripData?
) {
    if (userTripData == null) {
        return
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ComposeTextView.TitleTextView(
                text = userTripData.tripName
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color = LocalCustomColors.current.secondaryBackground, CircleShape)
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Default.Share,
                        modifier = Modifier.size(20.dp),
                        contentDescription = "Share Trip",
                        tint = LocalCustomColors.current.primaryBackground
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TripSummaryDetailItem(
                    Icons.Default.DateRange,
                    "${userTripData.startDate} - ${userTripData.endDate}"
                )

                Spacer(Modifier.height(6.dp))

                TripSummaryDetailItem(
                    Icons.Default.LocationOn,
                    userTripData.tripLocation?.cityName ?: ""
                )

                Spacer(Modifier.height(6.dp))

                TripSummaryDetailItem(Icons.Filled.Face, userTripData.visibility.value)
            }

            Box(
                modifier = Modifier
                    .background(LocalCustomColors.current.secondaryBackground, RoundedCornerShape(8.dp))
                    .padding(16.dp, 8.dp)
                    .clickable {  }
            ) {
                ComposeTextView.TextView(
                    text = stringResource(R.string.accept),
                    textColor = LocalCustomColors.current.primaryBackground
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        TripMatesList(userTripData.tripMates)

        Spacer(Modifier.height(16.dp))

        ComposeButtonView.PrimaryButtonView(
            text = stringResource(R.string.edit_details),
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()
        ) { }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TripMatesList(tripMates: List<User>) {
    var completeListVisibility by remember {
        mutableStateOf(false)
    }
    val defaultShown = 7

    val itemsToTake = if (completeListVisibility) Int.MAX_VALUE else defaultShown
    val visibleList = tripMates.take(itemsToTake)
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        visibleList.forEach { user ->
            ComposeImageView.CircularImageView(
                imageURI = user.profilePicture ?: "",
                diameter = 36.dp
            )
        }

        if (completeListVisibility) {
            ComposeTextView.TextView(
                text = "+${tripMates.size - defaultShown}",
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    completeListVisibility = true
                }
            )
        }
    }
}

@Composable
private fun TripSummaryDetailItem(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = "trip time",
            modifier = Modifier.size(14.dp),
            tint = LocalCustomColors.current.secondaryBackground
        )

        Spacer(Modifier.width(4.dp))

        ComposeTextView.TextView(
            text = text
        )
    }
}

@Composable
private fun UserTripDetailToolbar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color = LocalCustomColors.current.secondaryBackground, CircleShape)
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LocalCustomColors.current.primaryBackground
                )
            }
        }

        ComposeTextView.TextView(
            text = stringResource(R.string.see_trips),
            textColor = LocalCustomColors.current.secondaryBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {  }
        )
    }
}

@Composable
private fun ActionTile (
    item: TripActionItem,
    onClick: (key: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 12.dp, 8.dp, 12.dp)
            .clickable { onClick(item.key) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        ComposeImageView.ImageViewWitDrawableId(
            imageId = item.vectorId,
            modifier = Modifier.height(dimensionResource(id = R.dimen.module_36)),
            contentDescription = stringResource(id = item.title)
        )

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(32.dp, 0.dp, 0.dp, 0.dp)
        ) {
            ComposeTextView.TextView(
                text = stringResource(id = item.title),
                fontSize = with(LocalDensity.current) {
                    dimensionResource(id = R.dimen.module_18sp).toSp()
                },
                fontWeight = FontWeight.W500
            )
            ComposeTextView.TextView(
                text = stringResource(id = item.title),
                fontSize = with(LocalDensity.current) {
                    dimensionResource(id = R.dimen.module_16sp).toSp()
                },
                fontWeight = FontWeight.W400
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ComposeImageView.ImageViewWitDrawableId(
            imageId = R.drawable.ic_right_arrow,
            modifier = Modifier.height(dimensionResource(id = R.dimen.module_20)),
            contentDescription = "right arrow"
        )
    }
}

private fun takeAction(navController: NavController, key: String) {
    when (key) {
        Constants.TripDetailScreen.GENERAL -> {}
        Constants.TripDetailScreen.ATTACHMENTS-> {}
        Constants.TripDetailScreen.GROUP -> {}
        Constants.TripDetailScreen.ITINERARY-> {}
        Constants.TripDetailScreen.EXPENSES -> {}
    }
}