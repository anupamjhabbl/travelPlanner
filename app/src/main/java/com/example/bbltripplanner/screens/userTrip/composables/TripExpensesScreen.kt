package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.CommonLifecycleAwareLaunchedEffect
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.utils.DateUtils.toFormattedDateString
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.ExpenseItem
import com.example.bbltripplanner.screens.userTrip.viewModels.ExpenseIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.ExpenseViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch

@Composable
fun TripExpensesScreen(
    tripId: String?,
    viewModel: ExpenseViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val message = stringResource(R.string.min_budget_message)
    val tripExpenses by viewModel.expenseStatus.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.generic_error)
    var tripInitializationPopup by remember {
        mutableStateOf(false)
    }
    var isLoading by remember { mutableStateOf(false) }
    val successMessage = stringResource(R.string.expesnes_deleted_success)
    var budgetInput by remember { mutableStateOf("") }

    LaunchedEffect(tripExpenses) {
        if (!tripExpenses.isLoading && tripExpenses.error == null && tripExpenses.data == null) {
            tripInitializationPopup = true
        }
    }

    CommonLifecycleAwareLaunchedEffect(viewModel.deleteExpenseStatus) { viewEffect ->
        when (viewEffect) {
            is ExpenseIntent.DeleteViewEffect.DeleteExpenseError -> {
                isLoading = false
                ComposeViewUtils.showToast(context, viewEffect.message)
            }
            ExpenseIntent.DeleteViewEffect.DeleteExpenseLoading -> {
                isLoading = true
            }
            ExpenseIntent.DeleteViewEffect.DeleteExpenseSuccess -> {
                isLoading = false
                ComposeViewUtils.showToast(context, successMessage)
            }
        }
    }

    if (tripInitializationPopup) {
        AlertDialog(
            onDismissRequest = { tripInitializationPopup = false },
            title = {
                ComposeTextView.TitleTextView(
                    text = stringResource(R.string.initiate_expense_title),
                    fontSize = 20.sp
                )
            },
            text = {
                Column {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.initiate_expense_message),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = budgetInput,
                        onValueChange = {
                            if (it.isEmpty() || it.toDoubleOrNull() != null || (it.count { char -> char == '.' } <= 1 && it.all { char -> char.isDigit() || char == '.' })) {
                                budgetInput = it
                            }
                        },
                        label = { ComposeTextView.TextView(text = stringResource(R.string.enter_budget)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LocalCustomColors.current.secondaryBackground,
                            unfocusedBorderColor = LocalCustomColors.current.textColor.copy(alpha = 0.5f),
                            cursorColor = LocalCustomColors.current.secondaryBackground,
                            focusedLabelColor = LocalCustomColors.current.secondaryBackground
                        )
                    )
                }
            },
            confirmButton = {
                ComposeButtonView.PrimaryButtonView(
                    text = stringResource(R.string.add),
                    fontSize = 16.sp,
                    onClick = {
                        val budget = budgetInput.toDoubleOrNull() ?: 0.0
                        if (budget != 0.0) {
                            tripInitializationPopup = false
                            tripId?.let {
                                viewModel.processEvent(
                                    ExpenseIntent.ViewEvent.InitiateBudget(
                                        it,
                                        budget
                                    )
                                )
                            }
                        } else {
                            ComposeViewUtils.showToast(context, message)
                        }
                    }
                )
            },
            dismissButton = {
                ComposeButtonView.SecondaryButtonView(
                    text = stringResource(R.string.cancel),
                    fontSize = 16.sp,
                    onClick = { tripInitializationPopup = false }
                )
            },
            containerColor = LocalCustomColors.current.primaryBackground,
            shape = RoundedCornerShape(12.dp)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if ((tripExpenses.isLoading && tripExpenses.data == null) || isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ComposeViewUtils.FullScreenLoading()
            }
        } else if (tripExpenses.data == null || tripExpenses.error != null) {
            ComposeViewUtils.FullScreenErrorComposable(
                Pair(
                    errorMessage,
                    tripExpenses.error ?: ""
                )
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(32.dp)
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
                                    tint = LocalCustomColors.current.secondaryBackground,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(Modifier.width(8.dp))

                        ComposeTextView.TextView(
                            text = stringResource(R.string.expenses),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W600,
                            textColor = LocalCustomColors.current.secondaryBackground
                        )
                    }

                    Row(
                        modifier = Modifier
                            .size(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    CommonNavigationChannel.navigateTo(
                                        NavigationAction.Navigate(
                                            AppNavigationScreen.NotificationScreen.route
                                        )
                                    )
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notification",
                                tint = LocalCustomColors.current.secondaryBackground,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                ExpenseBanner(
                    modifier = Modifier.fillMaxWidth(),
                    tripExpenses.data!!.budget,
                    tripExpenses.data!!.expense,
                    tripExpenses.data!!.left
                )

                Spacer(Modifier.height(16.dp))

                ToolBoxRowView(tripId)

                Spacer(Modifier.height(16.dp))

                LazyColumn {
                    items(
                        items = tripExpenses.data!!.expenses,
                        key = { it.id }
                    ) { item ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { state ->
                                if (state == SwipeToDismissBoxValue.EndToStart) {
                                    tripId?.let {
                                        viewModel.processEvent(
                                            ExpenseIntent.ViewEvent.DeleteExpense(
                                                item.id
                                            )
                                        )
                                    }
                                    true
                                } else false
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(Color.Red),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier.padding(end = 24.dp)
                                    )
                                }
                            }
                        ) {
                            ExpenseItem(item)
                        }

                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
        if (tripExpenses.isLoading && tripExpenses.data != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                ComposeViewUtils.FullScreenLoading()
            }
        }
    }
}

@Composable
fun ColumnScope.ToolBoxRowView(
    tripId: String?
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.align(Alignment.End)
    ) {
        Button(
            modifier = Modifier.height(38.dp).wrapContentWidth(),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                tripId?.let {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(
                                AppNavigationScreen.AddExpenseScreen.createRoute(it)
                            )
                        )
                    }
                }
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = LocalCustomColors.current.secondaryBackground
            ),
        ) {
            Row {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = LocalCustomColors.current.primaryButtonText
                )

                Spacer(Modifier.width(2.dp))

                ComposeTextView.TextView(
                    text = stringResource(R.string.add),
                    textColor = LocalCustomColors.current.primaryButtonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        Button(
            modifier = Modifier.height(38.dp).wrapContentWidth().padding(end = 16.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                tripId?.let {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(
                                AppNavigationScreen.ExpenseSettlementScreen.createRoute(it)
                            )
                        )
                    }
                }
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = LocalCustomColors.current.secondaryBackground
            ),
        ) {
            Row {
                Icon(
                    Icons.Default.Payment,
                    contentDescription = "Add",
                    tint = LocalCustomColors.current.primaryButtonText
                )

                Spacer(Modifier.width(4.dp))

                ComposeTextView.TextView(
                    text = stringResource(R.string.settlements),
                    textColor = LocalCustomColors.current.primaryButtonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )
            }
        }
    }
}

@Composable
private fun ExpenseBanner(
    modifier: Modifier,
    budget: Double,
    expense: Double,
    left: Double
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {

        ComposeImageView.ImageViewWitDrawableId(
            imageId = R.drawable.expense_banner,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 16.dp)
        ) {
            ComposeTextView.TextView(
                text = stringResource(R.string.rupee_formatting, expense),
                fontSize = 24.sp,
                fontWeight = FontWeight.W600,
                textColor = Color.Black
            )

            ComposeTextView.TextView(
                text = stringResource(R.string.total_spent),
                textColor = Color.Black
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(12.dp)
                .height(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ComposeTextView.TextView(
                text = stringResource(R.string.budget, budget.toString()),
                modifier = Modifier.padding(horizontal = 8.dp),
                textColor = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )

            ComposeTextView.TextView(
                text = stringResource(R.string.remaining, left.toString()),
                textColor = if (left >= 0) Color.Green else Color.Red,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )
        }
    }
}

@Composable
private fun ExpenseItem(item: ExpenseItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.fadedBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFFDF2ED), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.type.icon,
                    contentDescription = item.type.value,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    tint = LocalCustomColors.current.secondaryBackground
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                ComposeTextView.TextView(
                    text = item.description,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textColor = LocalCustomColors.current.textColor
                )
                ComposeTextView.TextView(
                    text = stringResource(R.string.paid_by_name, item.paidBy.name),
                    fontSize = 14.sp,
                    textColor = LocalCustomColors.current.textColor
                )
                ComposeTextView.TextView(
                    text = "Split: 3 people",
                    fontSize = 14.sp,
                    textColor = LocalCustomColors.current.textColor
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                ComposeTextView.TextView(
                    text = stringResource(R.string.rupee_formatting, item.amount.toString()),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textColor = LocalCustomColors.current.textColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                ComposeTextView.TextView(
                    text = item.date.toFormattedDateString(),
                    fontSize = 14.sp,
                    textColor = LocalCustomColors.current.textColor
                )
            }
        }
    }
}
