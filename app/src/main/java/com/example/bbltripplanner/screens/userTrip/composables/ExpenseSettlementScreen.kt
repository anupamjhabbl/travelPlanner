package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
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
import com.example.bbltripplanner.screens.userTrip.entity.SettlementItem
import com.example.bbltripplanner.screens.userTrip.entity.SettlementResponseType
import com.example.bbltripplanner.screens.userTrip.viewModels.ExpenseIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.ExpenseViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun ExpenseSettlementScreen(
    viewModel: ExpenseViewModel
) {
    val settlements by viewModel.settlementStatus.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.generic_error)

    LaunchedEffect(Unit) {
        viewModel.processEvent(ExpenseIntent.ViewEvent.FetchSettlements)
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
            ComposeViewUtils.FullScreenErrorComposable(
                Pair(
                    errorMessage,
                    settlements.error ?: ""
                )
            )
        } else {
            settlements.data?.let { data ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LocalCustomColors.current.primaryBackground)
                ) {
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
                                tint = if (data.overallType == SettlementResponseType.RECEIVE) LocalCustomColors.current.success else LocalCustomColors.current.error,
                                modifier = Modifier.size(32.dp)
                            )

                            Spacer(Modifier.width(8.dp))

                            ComposeTextView.TitleTextView(
                                text = stringResource(if (data.overallType == SettlementResponseType.RECEIVE) R.string.receive_money else R.string.pay_money),
                                fontSize = 24.sp
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        val cardBg =
                            if (data.overallType == SettlementResponseType.RECEIVE) LocalCustomColors.current.success.copy(alpha = 0.1f) else LocalCustomColors.current.error.copy(
                                alpha = 0.1f
                            )
                        val accentColor =
                            if (data.overallType == SettlementResponseType.RECEIVE) LocalCustomColors.current.success else LocalCustomColors.current.error

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(cardBg)
                                .padding(24.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ComposeTextView.TitleTextView(
                                    text = stringResource(
                                        R.string.rupee_formatting,
                                        data.totalSettlementAmount.toInt().toString()
                                    ),
                                    fontSize = 32.sp,
                                    textColor = accentColor
                                )
                                Spacer(Modifier.width(8.dp))
                                ComposeTextView.TextView(
                                    text = stringResource(if (data.overallType == SettlementResponseType.RECEIVE) R.string.to_receive else R.string.to_pay),
                                    fontSize = 14.sp,
                                    textColor = accentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        ComposeTextView.TitleTextView(
                            text = stringResource(if (data.overallType == SettlementResponseType.RECEIVE) R.string.paid_by else R.string.paid_to),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    LocalCustomColors.current.defaultImageCardColor.copy(
                                        alpha = 0.3f
                                    )
                                )
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(1.dp)
                        ) {
                            items(data.settlements) { item ->
                                SettlementRow(item, data.overallType == SettlementResponseType.RECEIVE)
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
    isReceiveMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ComposeImageView.CircularImageView(
                imageURI = item.user.profilePicture ?: "",
                diameter = 40.dp
            )

            Spacer(Modifier.width(12.dp))

            ComposeTextView.TextView(
                text = item.user.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        ComposeTextView.TextView(
            text = stringResource(R.string.rupee_formatting, item.amount),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textColor = if (isReceiveMode) LocalCustomColors.current.success else LocalCustomColors.current.error
        )
    }
}