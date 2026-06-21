package com.example.bbltripplanner.screens.userTrip.composables

import android.content.Context
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.utils.DateUtils
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
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostingInitScreen() {
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
    val viewModel: PostingInitViewModel = koinViewModel(parameters = { parametersOf(null) })
    val postingFormData by viewModel.tripFormData.collectAsStateWithLifecycle()
    val queryString by viewModel.searchQuery.collectAsStateWithLifecycle()
    var locationList by remember {
        mutableStateOf(emptyList<Location>())
    }
    var isLocationError by remember {
        mutableStateOf(false)
    }
    var locationErrorMessage by remember {
        mutableStateOf<String?>(null)
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

    LaunchedEffect(Unit) { 
        viewModel.viewEffect.collectLatest { viewEffect ->
            when (viewEffect) {
                is PostingInitIntent.ViewEffect.GoNext -> {
                    tripIdToNavigate = viewEffect.tripData.tripId
                    showSuccessPopup = true
                }

                is PostingInitIntent.ViewEffect.ShowError -> {
                    val message = getMessage(context, viewEffect.errorMessage) ?: genericMessage
                    ComposeViewUtils.showToast(context, message)
                }

                is PostingInitIntent.ViewEffect.ShowSuggestions -> {
                    locationList = viewEffect.suggestions
                    isLocationError = viewEffect.isError
                    locationErrorMessage = getMessage(context, viewEffect.errorMessage)
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
    }

    if (showSuccessPopup) {
        ComposeViewUtils.SuccessPopup(
            message = stringResource(R.string.trip_created_success),
            onConfirm = {
                showSuccessPopup = false
                scope.launch {
                    moveToNextPage(
                        false,
                        tripIdToNavigate,
                        {},
                        { ComposeViewUtils.showToast(context, genericMessage) }
                    )
                }
            }
        )
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

    if (isLoading) {
        ComposeViewUtils.FullScreenLoading()
        return
    }

    if (showFullScreenError != null) {
        ComposeViewUtils.FullScreenErrorComposable(Pair(stringResource(R.string.generic_error), showFullScreenError!!))
        return
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
                    userList = userList.value?.followers ?: emptyList(),
                    isFollowersLoading = isFollowersLoading,
                    isError = userList.value?.isError ?: false,
                    errorMessage = getMessage(context, userList.value?.errorMessage)
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
                    locationList = locationList,
                    searchQuery = queryString,
                    isLocationLoading = isLocationLoading,
                    isLocationError = isLocationError,
                    errorMessage = locationErrorMessage,
                    onQueryChanged = { query ->
                        isLocationError = false
                        viewModel.processEvent(PostingInitIntent.ViewEvent.OnQueryChanged(query))
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

            Spacer(Modifier.height(24.dp))

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
            val startDateMessage = stringResource(R.string.trip_start_date_please)
            val endDateMessage = stringResource(R.string.trip_end_date_please)
            BottomSaveButton {
                if (
                        isValidStartDate(
                            postingFormData.startDate,
                            {
                                ComposeViewUtils.showToast(context, startDateMessage)
                            }
                        )
                        && isValidEndDate(
                            postingFormData.endDate,
                            {
                                ComposeViewUtils.showToast(context, endDateMessage)
                            }
                        )
                        && isValidTripName(
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
                    viewModel.processEvent(PostingInitIntent.ViewEvent.SaveAndContinue)
                }
            }
        }
    }
}

fun getMessage(
    context: Context,
    errorMessage: String?
): String? {
    if (errorMessage == null) return null
    return when (errorMessage) {
        Constants.ErrorType.NETWORK_ERROR -> context.getString(R.string.no_internet_connection)
        Constants.ErrorType.SERVER_ERROR -> context.getString(R.string.something_went_wrong)
        Constants.ErrorType.NOT_FOUND -> context.getString(R.string.nothing_to_show)
        Constants.ErrorType.NOT_AUTHORIZED -> context.getString(R.string.not_authorized_subtitle)
        Constants.ErrorType.NO_LOCATION_AVAILABLE -> context.getString(R.string.no_location_availaible)
        else -> errorMessage
    }
}

fun isValidStartDate(
    startDate: Long?,
    onFail: () -> Unit
): Boolean {
    if (startDate == null) {
        onFail()
        return false
    } else {
        return true
    }
}

fun isValidEndDate(
    endDate: Long?,
    onFail: () -> Unit
): Boolean {
    if (endDate == null) {
        onFail()
        return false
    } else {
        return true
    }
}

fun isValidTripName(
    tripName: String,
    onFail: () -> Unit
): Boolean {
    if (tripName.isEmpty()) {
        onFail()
        return false
    } else {
        return true
    }
}

fun isValidLocation(
    whereTo: Location?,
    onFail: () -> Unit
): Boolean {
    if (whereTo == null) {
        onFail()
        return false
    } else {
        return true
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostingInitScreenToolbar(
    visibility: TripVisibility,
    onChange: (tripVisibility: TripVisibility) -> Unit
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
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

        val itemIcons = remember {
            listOf(Icons.Default.Lock, Icons.Default.Public)
        }
        val selectedIcon = if (visibility == TripVisibility.PUBLIC) Icons.Default.Public else Icons.Default.Lock

        ComposeViewUtils.ExposedDropDownMenu(
            itemList = TripVisibility.entries.map { it.value.replaceFirstChar { char -> char.uppercase() } },
            selected = visibility.value.replaceFirstChar { char -> char.uppercase() },
            modifier = Modifier
                .background(color = LocalCustomColors.current.secondaryBackground, RoundedCornerShape(50))
                .height(38.dp)
                .width(130.dp)
                .padding(horizontal = 16.dp),
            textColor = LocalCustomColors.current.primaryBackground,
            itemIcons = itemIcons,
            selectedIcon = selectedIcon
        ) {
            onChange(TripVisibility.getEnum(it))
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
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
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

@Composable
fun UnifiedTripDatePickerCard(
    startDate: Long?,
    endDate: Long?,
    startDatePlaceholder: String,
    endDatePlaceholder: String,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    val customColors = LocalCustomColors.current
    val formattedStart = startDate?.let {
        val timeZone = kotlinx.datetime.TimeZone.currentSystemDefault()
        val date = kotlin.time.Instant.fromEpochMilliseconds(it).toLocalDateTime(timeZone).date
        val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
        "$month ${date.day}, ${date.year}"
    } ?: startDatePlaceholder

    val formattedEnd = endDate?.let {
        val timeZone = kotlinx.datetime.TimeZone.currentSystemDefault()
        val date = kotlin.time.Instant.fromEpochMilliseconds(it).toLocalDateTime(timeZone).date
        val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
        "$month ${date.day}, ${date.year}"
    } ?: endDatePlaceholder

    val duration = if (startDate != null && endDate != null) {
        val days = ((endDate - startDate) / (24 * 60 * 60 * 1000)).toInt() + 1
        "$days ${if (days == 1) "day" else "days"}"
    } else null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(customColors.defaultImageCardColor.copy(alpha = 0.2f))
            .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onStartDateClick() },
                horizontalAlignment = Alignment.Start
            ) {
                ComposeTextView.TextView(
                    text = "DEPARTURE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    textColor = customColors.hintTextColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = customColors.secondaryBackground,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    ComposeTextView.TextView(
                        text = formattedStart,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textColor = customColors.titleTextColor
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                if (duration != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(customColors.secondaryBackground.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        ComposeTextView.TextView(
                            text = duration,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textColor = customColors.secondaryBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = customColors.secondaryBackground,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onEndDateClick() },
                horizontalAlignment = Alignment.End
            ) {
                ComposeTextView.TextView(
                    text = "RETURN",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    textColor = customColors.hintTextColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = customColors.secondaryBackground,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    ComposeTextView.TextView(
                        text = formattedEnd,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textColor = customColors.titleTextColor
                    )
                }
            }
        }
    }
}

suspend fun moveToNextPage(isEdit: Boolean, tripId: String?, showSuccess: () -> Unit, showError: () -> Unit) {
    if (tripId == null) {
        showError()
        return
    }
    showSuccess()
    val popUpTo = if (isEdit) AppNavigationScreen.EditTripScreen.route else AppNavigationScreen.AddScreen.route
    CommonNavigationChannel.navigateTo(
        NavigationAction.Navigate(
            AppNavigationScreen.UserTripDetailScreen.createRoute(tripId)
        ) {
            popUpTo(popUpTo) {
                inclusive = true
            }
            launchSingleTop = true
        }
    )
}