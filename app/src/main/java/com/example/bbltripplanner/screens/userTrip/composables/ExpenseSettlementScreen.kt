package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.composables.ToolBarView
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.Currency
import com.example.bbltripplanner.screens.userTrip.entity.SettlementItem
import com.example.bbltripplanner.screens.userTrip.entity.SettlementResponseType
import com.example.bbltripplanner.screens.userTrip.viewModels.ExpenseIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.ExpenseViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch

@Composable
fun ExpenseSettlementScreen(
    viewModel: ExpenseViewModel
) {
    val settlements by viewModel.settlementStatus.collectAsStateWithLifecycle()
    val tripExpenses by viewModel.expenseStatus.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.generic_error)
    val currency = tripExpenses.data?.currency ?: Currency.INR
    val customColors = LocalCustomColors.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.processEvent(ExpenseIntent.ViewEvent.FetchSettlements)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
    ) {
        ToolBarView.SimpleToolbarWithBackButton(title = stringResource(R.string.settlements)) {
            scope.launch {
                CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (settlements.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ComposeViewUtils.FullScreenLoading()
                }
            } else if (settlements.error != null) {
                val error = settlements.error ?: ""
                if (error == ExpenseViewModel.SETTLEMENT_PENDING_ERROR) {
                    ComposeViewUtils.FullScreenErrorComposable(
                        errorStrings = Pair(
                            stringResource(R.string.settlement_not_available_title),
                            stringResource(R.string.settlement_not_available_description)
                        ),
                        imageId = R.drawable.ic_expenses
                    )
                } else {
                    ComposeViewUtils.FullScreenErrorComposable(
                        Pair(
                            errorMessage,
                            error
                        )
                    )
                }
            } else {
                settlements.data?.let { data ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Icon(
                                imageVector = if (data.overallType == SettlementResponseType.RECEIVE) Icons.Default.CheckCircle else Icons.Default.AccountBalance,
                                contentDescription = null,
                                tint = if (data.overallType == SettlementResponseType.RECEIVE) customColors.success else customColors.error,
                                modifier = Modifier.size(28.dp)
                            )

                            Spacer(Modifier.width(8.dp))

                            ComposeTextView.TitleTextView(
                                text = stringResource(if (data.overallType == SettlementResponseType.RECEIVE) R.string.receive_money else R.string.pay_money),
                                fontSize = 20.sp,
                                textColor = customColors.titleTextColor
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        val cardBg = if (data.overallType == SettlementResponseType.RECEIVE) {
                            customColors.success.copy(alpha = 0.12f)
                        } else {
                            customColors.error.copy(alpha = 0.12f)
                        }
                        val accentColor = if (data.overallType == SettlementResponseType.RECEIVE) {
                            customColors.success
                        } else {
                            customColors.error
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(cardBg)
                                .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                                .padding(24.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ComposeTextView.TitleTextView(
                                    text = stringResource(
                                        R.string.amount_formatting,
                                        currency.symbol,
                                        data.totalSettlementAmount.toInt().toString()
                                    ),
                                    fontSize = 32.sp,
                                    textColor = accentColor
                                )
                                Spacer(Modifier.width(10.dp))
                                ComposeTextView.TextView(
                                    text = stringResource(if (data.overallType == SettlementResponseType.RECEIVE) R.string.to_receive else R.string.to_pay),
                                    fontSize = 14.sp,
                                    textColor = accentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(Modifier.height(28.dp))

                        ComposeTextView.TitleTextView(
                            text = stringResource(if (data.overallType == SettlementResponseType.RECEIVE) R.string.paid_by else R.string.paid_to),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 12.dp),
                            textColor = customColors.titleTextColor
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(customColors.fadedBackground.copy(alpha = 0.35f))
                                .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(20.dp))
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(data.settlements) { item ->
                                SettlementRow(item, data.overallType == SettlementResponseType.RECEIVE, currency)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettlementRow(
    item: SettlementItem,
    isReceiveMode: Boolean,
    currency: Currency
) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(customColors.primaryBackground)
            .border(1.dp, customColors.defaultImageCardColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ComposeImageView.CircularImageView(
                imageURI = item.user.profilePicture ?: "",
                diameter = 36.dp
            )

            Spacer(Modifier.width(12.dp))

            ComposeTextView.TextView(
                text = item.user.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textColor = customColors.textColor
            )
        }

        ComposeTextView.TextView(
            text = stringResource(R.string.amount_formatting, currency.symbol, item.amount.toString()),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textColor = if (isReceiveMode) customColors.success else customColors.error
        )
    }
}