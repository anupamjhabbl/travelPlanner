package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.CommonLifecycleAwareLaunchedEffect
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.utils.DateUtils
import com.example.bbltripplanner.common.utils.DateUtils.toFormattedDateString
import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.screens.userTrip.viewModels.PostingInitIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.PostingInitViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostingEditScreen(
    tripId: String?
) {
    val viewModel: PostingInitViewModel = koinViewModel()
    var isLoading by remember {
        mutableStateOf(true)
    }
    var showFullScreenError by remember {
        mutableStateOf<String?>(null)
    }
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


    CommonLifecycleAwareLaunchedEffect(viewModel.viewEffect) { viewEffect ->
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

            is PostingInitIntent.ViewEffect.ShowFullScreenError -> {
                isLoading = false
                showFullScreenError = viewEffect.message
            }

            PostingInitIntent.ViewEffect.ShowSuccess -> {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        if (tripId != null) {
            viewModel.processEvent(PostingInitIntent.ViewEvent.GetTripDetails(tripId))
        } else {
            isLoading = false
            showFullScreenError = ""
        }
    }

    if (isLoading) {
        ComposeViewUtils.FullScreenLoading()
        return
    }

    if (showFullScreenError != null) {
        ComposeViewUtils.FullScreenErrorComposable(Pair(stringResource(R.string.generic_error), showFullScreenError!!))
        return
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
            val tripNameMessage = stringResource(R.string.trip_name_please)
            val whereToMessage = stringResource(R.string.trip_location_please)
            BottomSaveButton {
                if (
                    isValidTripName(
                        postingFormData.tripName,
                        {
                            ComposeViewUtils.showToast(context, tripNameMessage)
                        }
                    )
                    && isValidLocation(
                        postingFormData.whereTo, {
                            ComposeViewUtils.showToast(context, whereToMessage)
                        }
                    )
                ) {
                    viewModel.processEvent(PostingInitIntent.ViewEvent.UpdateAndContinue)
                }
            }
        }
    }
}