package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.utils.DateUtils
import com.example.bbltripplanner.common.utils.DateUtils.toFormattedDateString
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.screens.userTrip.entity.TripVisibility
import com.example.bbltripplanner.screens.userTrip.viewModels.PostingInitIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.PostingInitViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostingInitScreen() {
    val viewModel: PostingInitViewModel = koinViewModel()
    val postingFormData by viewModel.tripFormData.collectAsStateWithLifecycle()
    val queryString by viewModel.searchQuery.collectAsStateWithLifecycle()
    var locationList by remember {
        mutableStateOf(emptyList<Location>())
    }
    val userList = viewModel.inviteList.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val genericMessage = stringResource(R.string.generic_error)

    val startDate = stringResource(R.string.start_date)
    val endDate = stringResource(R.string.end_date)

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var showBottomSheet: BottomSheetType? by remember {
        mutableStateOf(null)
    }
    var showDatePicker: DatePickerType? by remember {
        mutableStateOf(null)
    }
    val startDateState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = DateUtils.FutureOrPresentSelectableDates(postingFormData.endDate)
    )
    val endDateState = rememberDatePickerState(
        initialSelectedDateMillis = postingFormData.startDate,
        selectableDates = DateUtils.DayAfterStartDateSelectableDates(startDateState.selectedDateMillis)
    )
    val scrollState = rememberScrollState()
    val bottomHeight = 80f

    LaunchedEffect(Unit) { 
        viewModel.viewEffect.collectLatest { viewEffect ->
            when (viewEffect) {
                is PostingInitIntent.ViewEffect.GoNext -> moveToNextPage(
                    viewEffect.tripData.tripId,
                    {
                        ComposeViewUtils.showToast(context, viewEffect.tripData.message)
                    }
                ) {
                    ComposeViewUtils.showToast(context, genericMessage)
                }

                PostingInitIntent.ViewEffect.ShowError -> ComposeViewUtils.showToast(context, genericMessage)

                is PostingInitIntent.ViewEffect.ShowSuggestions -> {
                    locationList = viewEffect.suggestions
                }
            }
        }
    }

    when (showDatePicker) {
        DatePickerType.START_DATE -> {
            CustomDatePicker(
                state = startDateState,
                onDismiss = {
                    showDatePicker = null
                }
            ) {
                viewModel.processEvent(PostingInitIntent.ViewEvent.UpdateStartDate(it))
            }
        }
        DatePickerType.END_DATE -> {
            if (postingFormData.startDate != null) {
                CustomDatePicker(
                    state = endDateState,
                    onDismiss = {
                        showDatePicker = null
                    }
                ) {
                    viewModel.processEvent(PostingInitIntent.ViewEvent.UpdateEndDate(it))
                }
            }
        }
        null -> {}
    }

    when (showBottomSheet) {
        BottomSheetType.USER_SELECTION -> {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                onDismissRequest = {
                    showBottomSheet = null
                },
                sheetState = sheetState,
                containerColor = LocalCustomColors.current.primaryBackground
            ) {
                InviteBottomSheet(
                    userList.value?.followers ?: emptyList()
                ) { user ->
                    showBottomSheet = null
                    viewModel.processEvent(PostingInitIntent.ViewEvent.AddTripMates(user))
                }
            }
        }
        BottomSheetType.LOCATION_SELECTION -> {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                onDismissRequest = {
                    showBottomSheet = null
                },
                sheetState = sheetState,
                containerColor = LocalCustomColors.current.primaryBackground
            ) {
                LocationBottomSheet(
                    locationList,
                    queryString,
                    {
                        viewModel.processEvent(PostingInitIntent.ViewEvent.OnQueryChanged(it))
                    }
                ) { location ->
                    viewModel.processEvent(PostingInitIntent.ViewEvent.UpdateTripLocation(location))
                    showBottomSheet = null
                }
            }
        }

        null -> {}
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            PostingInitScreenToolbar(postingFormData.visibility) { tripVisibility ->
                viewModel.processEvent(PostingInitIntent.ViewEvent.SetTripVisibility(tripVisibility))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp,16.dp, Dp(bottomHeight)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(60.dp))

            ComposeTextView.TitleTextView(
                text = stringResource(R.string.posting_heading),
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            ComposeTextView.TextView(
                text = stringResource(R.string.posting_subheading),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        showDatePicker = DatePickerType.START_DATE
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = LocalCustomColors.current.secondaryBackground
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    ComposeTextView.TextView(
                        text = postingFormData.startDate?.toFormattedDateString()  ?: startDate,
                        textColor = LocalCustomColors.current.secondaryBackground
                    )
                }
                OutlinedButton(
                    onClick = {
                        showDatePicker = DatePickerType.END_DATE
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = LocalCustomColors.current.secondaryBackground
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    ComposeTextView.TextView(
                        text = postingFormData.endDate?.toFormattedDateString() ?: endDate,
                        textColor = LocalCustomColors.current.secondaryBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = postingFormData.tripName,
                onValueChange = {
                    viewModel.processEvent(PostingInitIntent.ViewEvent.UpdateTripName(it))
                },
                placeholder = {
                    ComposeTextView.TitleTextView(
                        text = stringResource(R.string.trip_name),
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LocalCustomColors.current.secondaryBackground,
                    unfocusedBorderColor = LocalCustomColors.current.secondaryBackground,
                    errorBorderColor = LocalCustomColors.current.error,
                    disabledBorderColor = LocalCustomColors.current.fadedBackground
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            ClickableFieldBox(
                text = postingFormData.whereTo?.displayName ?: "",
                placeholder = stringResource(R.string.where_to)
            ) {
                showBottomSheet = BottomSheetType.LOCATION_SELECTION
            }

            Spacer(modifier = Modifier.height(24.dp))

            ComposeTextView.TitleTextView(
                text = stringResource(R.string.invite_tripmates),
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                postingFormData.invitedMembers.forEach { user ->
                    AssistChip(
                        onClick = {},
                        label = { ComposeTextView.TextView(user.name) },
                        leadingIcon = {
                            ComposeImageView.CircularImageView(
                                diameter = 24.dp,
                                imageURI = user.profilePicture ?: ""
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "remove",
                                tint = LocalCustomColors.current.fadedBackground,
                                modifier = Modifier.size(20.dp)
                                    .clickable {
                                        viewModel.processEvent(PostingInitIntent.ViewEvent.RemoveTripMates(user))
                                    }
                            )
                        },
                        shape = RoundedCornerShape(40),
                        border = BorderStroke(1.dp, color = LocalCustomColors.current.secondaryBackground)
                    )
                }

                AssistChip(
                    onClick = {
                        viewModel.processEvent(PostingInitIntent.ViewEvent.GetInviteList)
                        showBottomSheet = BottomSheetType.USER_SELECTION
                    },
                    label = { ComposeTextView.TextView(stringResource(R.string.add)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Add,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Add more"
                        )
                    },
                    shape = RoundedCornerShape(40),
                    border = BorderStroke(1.dp, color = LocalCustomColors.current.secondaryBackground)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dp(bottomHeight))
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            BottomSaveButton {
                viewModel.processEvent(PostingInitIntent.ViewEvent.SaveAndContinue)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostingInitScreenToolbar(
    visibility: TripVisibility,
    onChange: (tripVisibility: TripVisibility) -> Unit
) {
    val scope = rememberCoroutineScope()
    var expanded by remember {
        mutableStateOf(false)
    }

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
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.NavigateUp
                        )
                    }
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LocalCustomColors.current.primaryBackground
                )
            }
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            Box(
                modifier = Modifier
                    .menuAnchor()
                    .background(color = LocalCustomColors.current.secondaryBackground, RoundedCornerShape(50))
                    .height(32.dp)
                    .padding(horizontal = 16.dp)
                    .wrapContentWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ComposeTextView.TitleTextView(
                        visibility.value,
                        fontSize = 14.sp,
                        textColor = LocalCustomColors.current.primaryBackground
                    )
                    Spacer(Modifier.width(2.dp))
                    Icon(
                        Icons.Default.ArrowDropDown,
                        modifier = Modifier.size(24.dp),
                        contentDescription = "DropDown",
                        tint = LocalCustomColors.current.primaryBackground
                    )
                }
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
            ) {
                TripVisibility.entries.forEach { item ->
                    DropdownMenuItem(
                        text = { ComposeTextView.TextView(item.value) },
                        onClick = {
                            onChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomSaveButton(
    saveAndContinue: () -> Unit
) {
    ComposeButtonView.PrimaryButtonView (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 16.dp, 16.dp),
        text = stringResource(R.string.save_trip),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ) {
        saveAndContinue()
    }
}

@Composable
fun ClickableFieldBox(
    text: String,
    placeholder: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = LocalCustomColors.current.secondaryBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = LocalCustomColors.current.secondaryBackground
                )
                Spacer(modifier = Modifier.width(8.dp))

                if (text.isNotEmpty()) {
                    ComposeTextView.TextView(
                        text = text,
                        fontSize = 16.sp
                    )
                } else {
                    ComposeTextView.TextView(
                        text = placeholder,
                        fontSize = 16.sp,
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = LocalCustomColors.current.secondaryBackground
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    state: DatePickerState,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    val okText = stringResource(R.string.ok)
    val cancelText = stringResource(R.string.cancel)
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            DatePicker(state = state)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(onClick = onDismiss) {
                    Text(cancelText)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        state.selectedDateMillis?.let {
                            onConfirm(it)
                        }
                        onDismiss()
                    }
                ) {
                    Text(okText)
                }
            }
        }
    }
}

enum class BottomSheetType {
    USER_SELECTION,
    LOCATION_SELECTION
}

enum class DatePickerType {
    START_DATE,
    END_DATE
}

suspend fun moveToNextPage(tripId: String?, showSuccess: () -> Unit, showError: () -> Unit) {
    if (tripId == null) {
        showError()
        return
    }
    showSuccess()
    CommonNavigationChannel.navigateTo(
        NavigationAction.Navigate(
            AppNavigationScreen.UserTripDetailScreen.createRoute(tripId)
        )
    )
}