package com.example.bbltripplanner.screens.userTrip.composables

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.bbltripplanner.screens.userTrip.entity.Location
import com.example.bbltripplanner.screens.userTrip.viewModels.PostingInitIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.PostingInitViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostingEditScreen(
    tripId: String?
) {
    val viewModel: PostingInitViewModel = koinViewModel(parameters = { parametersOf(tripId) })
    var isLoading by remember {
        mutableStateOf(false)
    }
    var isFollowersLoading by remember {
        mutableStateOf(false)
    }
    var isLocationLoading by remember {
        mutableStateOf(false)
    }
    var showFullScreenError by remember {
        mutableStateOf<String?>(null)
    }
    var showSuccessPopup by remember {
        mutableStateOf(false)
    }
    var tripIdToNavigate by remember {
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
    val scope = rememberCoroutineScope()


    CommonLifecycleAwareLaunchedEffect(viewModel.viewEffect) { viewEffect ->
        when (viewEffect) {
            is PostingInitIntent.ViewEffect.GoNext -> {
                tripIdToNavigate = viewEffect.tripData.tripId
                showSuccessPopup = true
            }

            PostingInitIntent.ViewEffect.ShowError -> ComposeViewUtils.showToast(context, genericMessage)

            is PostingInitIntent.ViewEffect.ShowSuggestions -> {
                locationList = viewEffect.suggestions
            }

            is PostingInitIntent.ViewEffect.ShowFullScreenError -> {
                showFullScreenError = viewEffect.message
            }

            PostingInitIntent.ViewEffect.ShowSuccess -> {}
            PostingInitIntent.ViewEffect.HideFollowersLoading -> { isFollowersLoading = false }
            PostingInitIntent.ViewEffect.HideLoading -> { isLoading = false }
            PostingInitIntent.ViewEffect.HideLocationLoading -> { isLocationLoading = false }
            PostingInitIntent.ViewEffect.ShowFollowersLoading -> { isFollowersLoading = true }
            PostingInitIntent.ViewEffect.ShowLoading -> { isLoading = true }
            PostingInitIntent.ViewEffect.ShowLocationLoading -> { isLocationLoading = true }
        }
    }

    if (showSuccessPopup) {
        ComposeViewUtils.SuccessPopup(
            message = stringResource(R.string.trip_updated_success),
            onConfirm = {
                showSuccessPopup = false
                scope.launch {
                    moveToNextPage(
                        true,
                        tripIdToNavigate,
                        {},
                        { ComposeViewUtils.showToast(context, genericMessage) }
                    )
                }
            }
        )
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
                    userList.value?.followers ?: emptyList(),
                    isFollowersLoading
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
                    isLocationLoading,
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
        modifier = Modifier.fillMaxSize().padding(top = 8.dp)
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
            Spacer(Modifier.height(110.dp))

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

            UnifiedTripDatePickerCard(
                startDate = postingFormData.startDate,
                endDate = postingFormData.endDate,
                startDatePlaceholder = startDate,
                endDatePlaceholder = endDate,
                onStartDateClick = { showDatePicker = DatePickerType.START_DATE },
                onEndDateClick = { showDatePicker = DatePickerType.END_DATE }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = postingFormData.tripName,
                onValueChange = {
                    viewModel.processEvent(PostingInitIntent.ViewEvent.UpdateTripName(it))
                },
                placeholder = {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.trip_name),
                        fontSize = 15.sp,
                        textColor = LocalCustomColors.current.hintTextColor
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = LocalCustomColors.current.defaultImageCardColor.copy(alpha = 0.2f),
                    unfocusedContainerColor = LocalCustomColors.current.defaultImageCardColor.copy(alpha = 0.1f),
                    focusedBorderColor = LocalCustomColors.current.secondaryBackground,
                    unfocusedBorderColor = LocalCustomColors.current.secondaryBackground.copy(alpha = 0.7f),
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                postingFormData.invitedMembers.forEach { user ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(LocalCustomColors.current.defaultImageCardColor.copy(alpha = 0.3f))
                            .border(1.dp, LocalCustomColors.current.defaultImageCardColor, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ComposeImageView.CircularImageView(
                            diameter = 22.dp,
                            imageURI = user.profilePicture ?: ""
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        ComposeTextView.TextView(
                            text = user.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textColor = LocalCustomColors.current.titleTextColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "remove",
                            tint = LocalCustomColors.current.hintTextColor,
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .clickable {
                                    viewModel.processEvent(PostingInitIntent.ViewEvent.RemoveTripMates(user))
                                }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LocalCustomColors.current.secondaryBackground)
                        .clickable {
                            viewModel.processEvent(PostingInitIntent.ViewEvent.GetInviteList)
                            showBottomSheet = BottomSheetType.USER_SELECTION
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add more",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ComposeTextView.TextView(
                        text = stringResource(R.string.add),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textColor = Color.White
                    )
                }
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