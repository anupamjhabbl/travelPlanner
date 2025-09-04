package com.example.bbltripplanner.screens.posting.composables

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.screens.posting.entity.TripVisibility
import com.example.bbltripplanner.screens.posting.viewModels.PostingInitIntent
import com.example.bbltripplanner.screens.posting.viewModels.PostingInitViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostingInitScreen(
    navController: NavController
) {
    val viewModel: PostingInitViewModel = koinViewModel()
    val postingFormData by viewModel.tripFormData.collectAsState()
    val context = LocalContext.current
    val genericMessage = stringResource(R.string.generic_error)

    val startDate = stringResource(R.string.start_date)
    val endDate = stringResource(R.string.end_date)

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet: BottomSheetType? by remember {
        mutableStateOf(null)
    }
    val scrollState = rememberScrollState()
    val bottomHeight = 80f

    LaunchedEffect(Unit) { 
        viewModel.viewEffect.collectLatest { viewEffect ->
            when (viewEffect) {
                is PostingInitIntent.ViewEffect.GoNext -> moveToNextPage(navController, viewEffect.tripData.tripId) {
                    showError(context, genericMessage)
                }
                PostingInitIntent.ViewEffect.ShowError -> showError(context, genericMessage)
            }
        }
    }

    when (showBottomSheet) {
        BottomSheetType.USER_SELECTION -> {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = null
                },
                sheetState = sheetState,
                containerColor = colorResource(R.color.white)
            ) {
                InviteBottomSheet { user ->
                    viewModel.addTripMates(user)
                }
            }
        }
        BottomSheetType.LOCATION_SELECTION -> {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = null
                },
                sheetState = sheetState,
                containerColor = colorResource(R.color.white)
            ) {
                LocationBottomSheet { location ->
                    viewModel.updateTripLocation(location)
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
            PostingInitScreenToolbar(postingFormData.visibility, navController) { tripVisibility ->
                viewModel.setTripVisibility(tripVisibility)
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
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = colorResource(R.color.primary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    ComposeTextView.TextView(
                        text = postingFormData.startDate  ?: startDate,
                        textColor = colorResource(R.color.primary)
                    )
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = colorResource(R.color.primary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    ComposeTextView.TextView(
                        text = postingFormData.endDate ?: endDate,
                        textColor = colorResource(R.color.primary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = postingFormData.tripName,
                onValueChange = {
                    viewModel.updateTripName(it)
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
                    focusedBorderColor = colorResource(R.color.primary),
                    unfocusedBorderColor = colorResource(R.color.primary),
                    errorBorderColor = colorResource(R.color.error_red),
                    disabledBorderColor = colorResource(R.color.faded_primary)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            ClickableFieldBox(
                text = postingFormData.tripLocation?.cityName ?: "",
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
                postingFormData.tripMates.forEach { user ->
                    AssistChip(
                        onClick = {},
                        label = { ComposeTextView.TextView(user.name) },
                        leadingIcon = {
                            ComposeImageView.CircularImageView(
                                diameter = 24.dp,
                                imageURI = user.profilePicture
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "remove",
                                tint = colorResource(R.color.faded_primary),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        shape = RoundedCornerShape(40),
                        border = BorderStroke(1.dp, color = colorResource(R.color.primary))
                    )
                }

                AssistChip(
                    onClick = {
                        showBottomSheet = BottomSheetType.USER_SELECTION
                    },
                    label = { ComposeTextView.TextView("Add") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Add,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Add more"
                        )
                    },
                    shape = RoundedCornerShape(40),
                    border = BorderStroke(1.dp, color = colorResource(R.color.primary))
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
    navController: NavController,
    onChange: (tripVisibility: TripVisibility) -> Unit
) {
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
                .background(color = colorResource(R.color.primary), CircleShape)
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = colorResource(R.color.white)
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
                    .background(color = Color(0xFFF3F0FF), RoundedCornerShape(50))
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
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.width(2.dp))
                    Icon(
                        Icons.Default.ArrowDropDown,
                        modifier = Modifier.size(24.dp),
                        contentDescription = "DropDown",
                        tint = colorResource(R.color.primary)
                    )
                }
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
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
                color = colorResource(R.color.primary),
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
                    tint = colorResource(R.color.primary)
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
                tint = colorResource(R.color.primary)
            )
        }
    }
}

enum class BottomSheetType {
    USER_SELECTION,
    LOCATION_SELECTION
}

fun moveToNextPage(navController: NavController, tripId: String?, showError: () -> Unit) {
    if (tripId == null) {
        showError()
        return
    }
    navController.navigate(AppNavigationScreen.UserTripDetailScreen.createRoute(tripId))
}

fun showError(context: Context, stringResource: String) {
    Toast.makeText(context, stringResource, Toast.LENGTH_SHORT).show()
}