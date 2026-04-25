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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.composables.ToolBarView
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseType
import com.example.bbltripplanner.screens.userTrip.entity.SplitType
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddExpensesScreen(
    tripId: String?
) {
    val scope = rememberCoroutineScope()
    val paidByString = stringResource(R.string.paid_by)
    val scrollState = rememberScrollState()
    var isFollowersLoading by remember {
        mutableStateOf(false)
    }

    var paidBy by remember { mutableStateOf(paidByString) }
    var type by remember { mutableStateOf<ExpenseType?>(null) }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var split by remember { mutableStateOf(SplitType.EVERYONE) }
    val invitedMembers = listOf(
        User("1", "Anupam Kumar",null, null, null, 0, 0, 0, null, null),
        User("1", "Anupam Kumar",null, null, null, 0, 0, 0, null, null),
        User("1", "Anupam Kumar",null, null, null, 0, 0, 0, null, null),
        User("1", "Anupam Kumar",null, null, null, 0, 0, 0, null, null),
        User("1", "Anupam Kumar",null, null, null, 0, 0, 0, null, null),
        User("1", "Anupam Kumar",null, null, null, 0, 0, 0, null, null)
    )
    
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var showBottomSheet: ExpenseBottomSheetType? by remember {
        mutableStateOf(null)
    }

    if (showBottomSheet != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = null },
            sheetState = sheetState,
            containerColor = LocalCustomColors.current.primaryBackground,
            modifier = if (showBottomSheet == ExpenseBottomSheetType.USER_SELECTION) Modifier.fillMaxHeight() else Modifier
        ) {
            when (showBottomSheet) {
                ExpenseBottomSheetType.TYPE_SELECTION, ExpenseBottomSheetType.SPLIT_SELECTION -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        ComposeTextView.TitleTextView(
                            text = when (showBottomSheet) {
                                ExpenseBottomSheetType.TYPE_SELECTION -> stringResource(R.string.select_type)
                                ExpenseBottomSheetType.SPLIT_SELECTION -> stringResource(R.string.select_split_type)
                                else -> ""
                            },
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
                            when (showBottomSheet) {
                                ExpenseBottomSheetType.TYPE_SELECTION -> {
                                    items(ExpenseType.entries) { expenseType ->
                                        SelectionItem(
                                            label = expenseType.value,
                                            icon = expenseType.icon,
                                            isSelected = type == expenseType
                                        ) {
                                            type = expenseType
                                            showBottomSheet = null
                                        }
                                    }
                                }
                                ExpenseBottomSheetType.SPLIT_SELECTION -> {
                                    items(SplitType.entries) { splitType ->
                                        SelectionItem(
                                            label = splitType.value,
                                            icon = splitType.icon,
                                            isSelected = split == splitType
                                        ) {
                                            split = splitType
                                            showBottomSheet = null
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
                ExpenseBottomSheetType.USER_SELECTION -> {
                    InviteBottomSheet(
                        invitedMembers,
                        isFollowersLoading
                    ) { user ->
                        showBottomSheet = null
                    }
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.primaryBackground)
    ) {
        ToolBarView.SimpleToolbarWithBackButton(
            stringResource(R.string.add_expense)
        ) {
            scope.launch {
                CommonNavigationChannel.navigateTo(
                    NavigationAction.NavigateUp
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
                .weight(1f)
        ) {
            Box(modifier = Modifier.align(Alignment.End)) {
                ComposeViewUtils.ExposedDropDownMenu(
                    listOf("Anupam", "Harpreet", "Ayush"),
                    paidBy
                ) { paid ->
                    paidBy = paid
                }
            }

            Spacer(Modifier.height(24.dp))

            FieldLabel(stringResource(R.string.type))

            ClickableSelectorField(
                text = type?.value ?: "",
                placeholder = stringResource(R.string.select_type)
            ) {
                showBottomSheet = ExpenseBottomSheetType.TYPE_SELECTION
            }

            Spacer(Modifier.height(20.dp))

            FieldLabel(stringResource(R.string.description))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LocalCustomColors.current.secondaryBackground,
                    unfocusedBorderColor = LocalCustomColors.current.secondaryBackground,
                    cursorColor = LocalCustomColors.current.secondaryBackground
                )
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            LocalCustomColors.current.secondaryBackground,
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CurrencyRupee,
                        contentDescription = null,
                        tint = LocalCustomColors.current.secondaryBackground
                    )
                }

                Spacer(Modifier.width(12.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = {
                        ComposeTextView.TextView(
                            text = stringResource(R.string.amount),
                            fontSize = 16.sp,
                            textColor = LocalCustomColors.current.hintTextColor
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LocalCustomColors.current.secondaryBackground,
                        unfocusedBorderColor = LocalCustomColors.current.secondaryBackground,
                        cursorColor = LocalCustomColors.current.secondaryBackground
                    ),
                    keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(24.dp))


            FieldLabel(stringResource(R.string.split))

            ClickableSelectorField(
                text = split.value,
                placeholder = stringResource(R.string.select_split_type)
            ) {
                showBottomSheet = ExpenseBottomSheetType.SPLIT_SELECTION
            }

            if (split == SplitType.GROUP) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        invitedMembers.forEach { user ->
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

                                            }
                                    )
                                },
                                shape = RoundedCornerShape(40),
                                border = BorderStroke(
                                    1.dp,
                                    color = LocalCustomColors.current.secondaryBackground
                                )
                            )
                        }

                        AssistChip(
                            onClick = {
                                showBottomSheet = ExpenseBottomSheetType.USER_SELECTION
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
                            border = BorderStroke(
                                1.dp,
                                color = LocalCustomColors.current.secondaryBackground
                            )
                        )
                    }
                }
            }
        }

        ComposeButtonView.PrimaryButtonView(
            text = stringResource(R.string.add_expense),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ) {

        }
    }
}

@Composable
fun SelectionItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) LocalCustomColors.current.secondaryBackground else LocalCustomColors.current.defaultImageCardColor
    val contentColor = if (isSelected) LocalCustomColors.current.primaryBackground else LocalCustomColors.current.textColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ComposeTextView.TextView(
            text = label,
            fontSize = 12.sp,
            textColor = contentColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun FieldLabel(text: String) {
    ComposeTextView.TitleTextView(
        text = text,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun ClickableSelectorField(
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
                if (text.isNotEmpty()) {
                    ComposeTextView.TextView(
                        text = text,
                        fontSize = 16.sp
                    )
                } else {
                    ComposeTextView.TextView(
                        text = placeholder,
                        fontSize = 16.sp,
                        textColor = LocalCustomColors.current.hintTextColor
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

enum class ExpenseBottomSheetType {
    TYPE_SELECTION,
    SPLIT_SELECTION,
    USER_SELECTION
}
