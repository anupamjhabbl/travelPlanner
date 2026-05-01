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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.CommonLifecycleAwareLaunchedEffect
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.AddActivityRequest
import com.example.bbltripplanner.screens.userTrip.entity.ItineraryActivity
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryDetailIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.ItineraryDetailViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ItineraryDetailView(
    placeId: String?
) {
    val viewModel: ItineraryDetailViewModel = koinViewModel()
    val customColors = LocalCustomColors.current
    val context = LocalContext.current
    val activitiesStatus by viewModel.activitiesStatus.collectAsState()
    val actionStatus by viewModel.actionStatus.collectAsState()
    val errorMessage = stringResource(R.string.generic_error)
    rememberCoroutineScope()

    var showActivityDialog by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf<ItineraryActivity?>(null) }

    CommonLifecycleAwareLaunchedEffect(viewModel.viewEffect) { effect ->
        when (effect) {
            is ItineraryDetailIntent.ViewEffect.ErrorInActivityCreation -> {
                ComposeViewUtils.showToast(context, effect.message)
            }
        }
    }

    LaunchedEffect(placeId) {
        placeId?.let {
            viewModel.processEvent(ItineraryDetailIntent.ViewEvent.FetchActivities(it))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
    ) {
        if (activitiesStatus.isLoading) {
            CircularProgressIndicator(color = customColors.secondaryBackground)
        } else if (activitiesStatus.error != null) {
            ComposeViewUtils.FullScreenErrorComposable(
                Pair(
                    errorMessage,
                    activitiesStatus.error ?: ""
                )
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        PlaceHeader(imageUrl = "", name = activitiesStatus.data?.placeName ?: "", address = activitiesStatus.data?.location?.displayName ?: "")
                    }

                    item {
                        Column(
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.module_16))
                        ) {
                            ComposeTextView.TitleTextView(
                                text = stringResource(R.string.activities),
                                fontSize = 18.sp,
                                textColor = customColors.secondaryBackground
                            )
                        }
                    }

                    val activities = activitiesStatus.data?.itineraryActivities ?: emptyList()
                    if (activities.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ComposeTextView.TextView(text = stringResource(R.string.no_activities))
                            }
                        }
                    } else {
                        items(activities) { activity ->
                            ActivityItem(
                                activity = activity,
                                onEdit = {
                                    selectedActivity = activity
                                    showActivityDialog = true
                                },
                                onDelete = {
                                    activity.activityId?.let { activityId ->
                                        viewModel.processEvent(
                                            ItineraryDetailIntent.ViewEvent.DeleteActivity(
                                                activityId
                                            )
                                        )
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.module_16)))
                        }
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
                                    colors = listOf(Color.Black.copy(alpha = 0.15f), Color.Transparent)
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
                            selectedActivity = null
                            showActivityDialog = true
                        }
                    )
                }
            }
        }

        if (actionStatus.isLoading) {
            ComposeViewUtils.FullScreenLoading()
        }

        if (showActivityDialog) {
            AddEditActivityDialog(
                activity = selectedActivity,
                onDismiss = { showActivityDialog = false },
                onConfirm = { name, desc, startH, endH ->
                    val baseDate = activitiesStatus.data?.date ?: 0L
                    val request = AddActivityRequest(
                        activityName = name,
                        description = desc,
                        startTime = baseDate + (startH.toLong() * 3600 * 1000),
                        endTime = baseDate + (endH.toLong() * 3600 * 1000)
                    )
                    if (selectedActivity == null) {
                        placeId?.let {
                            viewModel.processEvent(ItineraryDetailIntent.ViewEvent.AddActivity(it, request))
                        }
                    } else {
                        selectedActivity?.activityId?.let {
                            viewModel.processEvent(ItineraryDetailIntent.ViewEvent.UpdateActivity(it, request))
                        }
                    }
                    showActivityDialog = false
                }
            )
        }
    }
}

@Composable
fun AddEditActivityDialog(
    activity: ItineraryActivity? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, Int) -> Unit
) {
    val customColors = LocalCustomColors.current
    var name by remember { mutableStateOf(activity?.activityName ?: "") }
    var description by remember { mutableStateOf(activity?.description ?: "") }
    

    val initialStartHour = if (activity != null) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = activity.startTime
        calendar.get(Calendar.HOUR_OF_DAY).let { if (it == 0) 24 else it }
    } else 9

    val initialEndHour = if (activity != null) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = activity.endTime
        calendar.get(Calendar.HOUR_OF_DAY).let { if (it == 0) 24 else it }
    } else 10

    var startHour by remember { mutableIntStateOf(initialStartHour) }
    var endHour by remember { mutableIntStateOf(initialEndHour) }

    val hoursList = (1..24).map { it.toString() }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = customColors.primaryBackground,
        title = {
            ComposeTextView.TitleTextView(
                text = if (activity == null) stringResource(R.string.add_new_activity) else stringResource(R.string.edit_activity),
                fontSize = 20.sp
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { ComposeTextView.TextView(stringResource(R.string.activity_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = customColors.secondaryBackground,
                        unfocusedBorderColor = customColors.fadedBackground,
                        focusedLabelColor = customColors.secondaryBackground
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { ComposeTextView.TextView(stringResource(R.string.description)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = customColors.secondaryBackground,
                        unfocusedBorderColor = customColors.fadedBackground,
                        focusedLabelColor = customColors.secondaryBackground
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        ComposeTextView.TextView("Start Time (H)")
                        ComposeViewUtils.ExposedDropDownMenu(
                            itemList = hoursList,
                            selected = startHour.toString(),
                            onChange = { startHour = it.toInt() }
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        ComposeTextView.TextView("End Time (H)")
                        ComposeViewUtils.ExposedDropDownMenu(
                            itemList = hoursList,
                            selected = endHour.toString(),
                            onChange = { endHour = it.toInt() }
                        )
                    }
                }
            }
        },
        confirmButton = {
            ComposeButtonView.PrimaryButtonView(
                text = "Confirm",
                onClick = { onConfirm(name, description, startHour, endHour) },
                modifier = Modifier.padding(bottom = 8.dp, end = 8.dp).width(100.dp).height(40.dp)
            )
        },
        dismissButton = {
            ComposeButtonView.SecondaryButtonView(
                text = "Cancel",
                onClick = onDismiss,
                modifier = Modifier.padding(bottom = 8.dp).width(100.dp).height(40.dp)
            )
        }
    )
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
fun ActivityItem(
    activity: ItineraryActivity,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val customColors = LocalCustomColors.current
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

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
                    text = "${timeFormatter.format(Date(activity.startTime))} - ${timeFormatter.format(Date(activity.endTime))}",
                    fontSize = 12.sp,
                    textColor = customColors.secondaryBackground,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.module_8)))
            ComposeTextView.TextView(
                text = activity.description ?: "",
                fontSize = 14.sp,
                textColor = customColors.textColor
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onEdit() },
                    tint = customColors.secondaryBackground
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onDelete() },
                    tint = Color.Red.copy(alpha = 0.7f)
                )
            }
        }
    }
}
