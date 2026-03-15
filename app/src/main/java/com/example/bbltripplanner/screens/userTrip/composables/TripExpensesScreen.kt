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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch

@Composable
fun TripExpensesScreen(
    tripId: String?
) {
    val scope = rememberCoroutineScope()
    val list = listOf("sjjd", "djjd", "djjdjd", "djjdj", "djjd", "jdhd", "djjdj", "jdjdj", "hdhdh")

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
            25000f,
            12000f,
            13000f
        )

        Spacer(Modifier.height(16.dp))

        ToolBoxRowView(tripId)

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(list) {
                ExpenseItem()

                Spacer(Modifier.height(16.dp))
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
    budget: Float,
    expense: Float,
    left: Float
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
private fun ExpenseItem() {
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
                ComposeTextView.TextView(
                    text = "🍔",
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                ComposeTextView.TextView(
                    text = "Dinner at Cafe",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textColor = LocalCustomColors.current.textColor
                )
                ComposeTextView.TextView(
                    text = "Paid by Jeevesh",
                    fontSize = 14.sp,
                    textColor = LocalCustomColors.current.textColor
                )
                ComposeTextView.TextView(
                    text = "Split: 3 people",
                    fontSize = 14.sp,
                    textColor = LocalCustomColors.current.textColor
                )
            }

            // Amount and Date
            Column(horizontalAlignment = Alignment.End) {
                ComposeTextView.TextView(
                    text = "₹ 420",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textColor = LocalCustomColors.current.textColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                ComposeTextView.TextView(
                    text = "12 Oct 2025",
                    fontSize = 14.sp,
                    textColor = LocalCustomColors.current.textColor
                )
            }
        }
    }
}