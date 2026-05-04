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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.CommonLifecycleAwareLaunchedEffect
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.composables.ToolBarView
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.AddExpenseRequest
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseType
import com.example.bbltripplanner.screens.userTrip.entity.SplitType
import com.example.bbltripplanner.screens.userTrip.viewModels.ExpenseIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.ExpenseViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddExpensesScreen(
    tripId: String?,
    viewModel: ExpenseViewModel
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val successMessage = stringResource(R.string.expesnes_added_success)
    val provideAllDetailMessage = stringResource(R.string.provide_details)
    
    val tripDataStatus by viewModel.tripData.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    
    var selectedPaidBy by remember { mutableStateOf<User?>(null) }
    var selectedType by remember { mutableStateOf<ExpenseType?>(null) }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var splitType by remember { mutableStateOf(SplitType.EVERYONE) }
    val selectedSplitUsers = remember { mutableStateListOf<User>() }

    var invitedMembers by remember {
        mutableStateOf<List<User>>(emptyList())
    }

    LaunchedEffect(tripDataStatus) {
        if (!tripDataStatus.isLoading && tripDataStatus.error == null && tripDataStatus.data != null) {
            invitedMembers = tripDataStatus.data!!.invitedMembers
        }
    }

    CommonLifecycleAwareLaunchedEffect(viewModel.addExpenseStatus) { viewEffect ->
        when (viewEffect) {
            is ExpenseIntent.AddViewEffect.AddExpenseError -> {
                isLoading = false
                ComposeViewUtils.showToast(context, viewEffect.message)
            }
            ExpenseIntent.AddViewEffect.AddExpenseLoading -> {
                isLoading = true
            }
            ExpenseIntent.AddViewEffect.AddExpenseSuccess -> {
                isLoading = false
                ComposeViewUtils.showToast(context, successMessage)
                CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
            }
        }
    }


    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showBottomSheet: ExpenseBottomSheetType? by remember { mutableStateOf(null) }

    if (showBottomSheet != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = null },
            sheetState = sheetState,
            containerColor = LocalCustomColors.current.primaryBackground,
            modifier = if (showBottomSheet == ExpenseBottomSheetType.USER_SELECTION) Modifier.fillMaxHeight() else Modifier
        ) {
            when (showBottomSheet) {
                ExpenseBottomSheetType.TYPE_SELECTION, ExpenseBottomSheetType.SPLIT_SELECTION -> {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        ComposeTextView.TitleTextView(
                            text = if (showBottomSheet == ExpenseBottomSheetType.TYPE_SELECTION) 
                                stringResource(R.string.select_type) else stringResource(R.string.select_split_type),
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.height(280.dp)
                        ) {
                            if (showBottomSheet == ExpenseBottomSheetType.TYPE_SELECTION) {
                                items(ExpenseType.entries) { expenseType ->
                                    SelectionItem(expenseType.value, expenseType.icon, selectedType == expenseType) {
                                        selectedType = expenseType
                                        showBottomSheet = null
                                    }
                                }
                            } else {
                                items(SplitType.entries) { split ->
                                    SelectionItem(split.value, split.icon, splitType == split) {
                                        splitType = split
                                        if (split == SplitType.EVERYONE) selectedSplitUsers.clear()
                                        showBottomSheet = null
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
                ExpenseBottomSheetType.USER_SELECTION -> {
                    InviteBottomSheet(invitedMembers, false) { user ->
                        if (!selectedSplitUsers.contains(user)) selectedSplitUsers.add(user)
                        showBottomSheet = null
                    }
                }
                else -> {}
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(LocalCustomColors.current.primaryBackground)) {
        ToolBarView.SimpleToolbarWithBackButton(stringResource(R.string.add_expense)) {
            scope.launch { CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp) }
        }

        Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState).weight(1f)) {
            // Paid By Dropdown
            Box(modifier = Modifier.align(Alignment.End)) {
                ComposeViewUtils.ExposedDropDownMenu(
                    invitedMembers.map { it.name },
                    selectedPaidBy?.name ?: stringResource(R.string.paid_by)
                ) { name ->
                    selectedPaidBy = invitedMembers.find { it.name == name }
                }
            }

            Spacer(Modifier.height(24.dp))

            FieldLabel(stringResource(R.string.type))
            ClickableSelectorField(selectedType?.value ?: "", stringResource(R.string.select_type)) {
                showBottomSheet = ExpenseBottomSheetType.TYPE_SELECTION
            }

            Spacer(Modifier.height(20.dp))

            FieldLabel(stringResource(R.string.description))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LocalCustomColors.current.secondaryBackground,
                    unfocusedBorderColor = LocalCustomColors.current.secondaryBackground,
                    cursorColor = LocalCustomColors.current.secondaryBackground
                )
            )

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp))
                        .border(1.dp, LocalCustomColors.current.secondaryBackground, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CurrencyRupee, null, tint = LocalCustomColors.current.secondaryBackground)
                }
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { ComposeTextView.TextView(stringResource(R.string.amount), fontSize = 16.sp, textColor = LocalCustomColors.current.hintTextColor) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LocalCustomColors.current.secondaryBackground,
                        unfocusedBorderColor = LocalCustomColors.current.secondaryBackground,
                        cursorColor = LocalCustomColors.current.secondaryBackground
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(24.dp))

            FieldLabel(stringResource(R.string.split))
            ClickableSelectorField(splitType.value, stringResource(R.string.select_split_type)) {
                showBottomSheet = ExpenseBottomSheetType.SPLIT_SELECTION
            }

            if (splitType == SplitType.GROUP) {
                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    selectedSplitUsers.forEach { user ->
                        AssistChip(
                            onClick = {},
                            label = { ComposeTextView.TextView(user.name) },
                            leadingIcon = {
                                ComposeImageView.CircularImageView(user.profilePicture ?: "", 24.dp)
                            },
                            trailingIcon = {
                                Icon(Icons.Default.Clear, "remove", tint = LocalCustomColors.current.secondaryBackground,
                                    modifier = Modifier.size(18.dp).clickable { selectedSplitUsers.remove(user) })
                            },
                            shape = RoundedCornerShape(40),
                            border = BorderStroke(1.dp, LocalCustomColors.current.secondaryBackground)
                        )
                    }
                    AssistChip(
                        onClick = { showBottomSheet = ExpenseBottomSheetType.USER_SELECTION },
                        label = { ComposeTextView.TextView(stringResource(R.string.add)) },
                        leadingIcon = { Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp)) },
                        shape = RoundedCornerShape(40),
                        border = BorderStroke(1.dp, LocalCustomColors.current.secondaryBackground)
                    )
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = LocalCustomColors.current.secondaryBackground)
            }
        } else {
            ComposeButtonView.PrimaryButtonView(
                text = stringResource(R.string.add_expense),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ) {
                if (tripId != null && selectedPaidBy != null && selectedType != null && amount.isNotEmpty()) {
                    val splitUserIds = when (splitType) {
                        SplitType.EVERYONE -> invitedMembers.map { it.id }
                        SplitType.SINGLE -> listOf(selectedPaidBy!!.id)
                        SplitType.GROUP -> selectedSplitUsers.map { it.id }
                    }
                    viewModel.processEvent(ExpenseIntent.ViewEvent.AddExpense(
                        tripId,
                        AddExpenseRequest(
                            description = description,
                            paidById = selectedPaidBy!!.id,
                            splitUserIds = splitUserIds,
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            type = selectedType!!
                        )
                    ))
                } else {
                    ComposeViewUtils.showToast(context, provideAllDetailMessage)
                }
            }
        }
    }
}

@Composable
fun SelectionItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) LocalCustomColors.current.secondaryBackground else LocalCustomColors.current.defaultImageCardColor
    val contentColor = if (isSelected) LocalCustomColors.current.primaryBackground else LocalCustomColors.current.textColor
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(backgroundColor).clickable { onClick() }.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, label, tint = contentColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        ComposeTextView.TextView(label, fontSize = 12.sp, textColor = contentColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun FieldLabel(text: String) {
    ComposeTextView.TitleTextView(text = text, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
fun ClickableSelectorField(text: String, placeholder: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .border(1.dp, LocalCustomColors.current.secondaryBackground, RoundedCornerShape(12.dp))
            .clickable { onClick() }.padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            ComposeTextView.TextView(text = text.ifEmpty { placeholder }, fontSize = 16.sp, 
                textColor = if (text.isEmpty()) LocalCustomColors.current.hintTextColor else LocalCustomColors.current.textColor)
            Icon(Icons.Default.KeyboardArrowDown, null, tint = LocalCustomColors.current.secondaryBackground)
        }
    }
}

enum class ExpenseBottomSheetType { TYPE_SELECTION, SPLIT_SELECTION, USER_SELECTION }
