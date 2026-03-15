package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
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
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.screens.userTrip.entity.SettlementItem
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun ExpenseSettlementScreen(
    tripId: String?
) {
    val isReceiveMode = true
    val totalAmount = 3240.0
    
    val settlementList = remember {
        listOf(
            SettlementItem(User("1", "Jeevesh", null, null, null, 0, 0, 0, null, null), 1200.0, isSettled = false, canRequest = true),
            SettlementItem(
                User("2", "Priya", null, null, null, 0, 0, 0, null, null),
                940.0,
                isSettled = true
            ),
            SettlementItem(User("3", "Ashish", null, null, null, 0, 0, 0, null, null), 640.0, isSettled = true),
            SettlementItem(User("4", "Vishal", null, null, null, 0, 0, 0, null, null), 460.0, isSettled = true)
        )
    }

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
                    imageVector = if (isReceiveMode) Icons.Default.CheckCircle else Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = if (isReceiveMode) LocalCustomColors.current.success else LocalCustomColors.current.error,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(Modifier.width(8.dp))

                ComposeTextView.TitleTextView(
                    text = stringResource(if (isReceiveMode) R.string.receive_money else R.string.pay_money),
                    fontSize = 24.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            val cardBg = if (isReceiveMode) LocalCustomColors.current.success.copy(alpha = 0.1f) else LocalCustomColors.current.error.copy(alpha = 0.1f)
            val accentColor = if (isReceiveMode) LocalCustomColors.current.success else LocalCustomColors.current.error

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
                        text = stringResource(R.string.rupee_formatting, totalAmount.toInt().toString()),
                        fontSize = 32.sp,
                        textColor = accentColor
                    )
                    Spacer(Modifier.width(8.dp))
                    ComposeTextView.TextView(
                        text = stringResource(if (isReceiveMode) R.string.to_receive else R.string.to_pay),
                        fontSize = 14.sp,
                        textColor = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            ComposeTextView.TitleTextView(
                text = stringResource(if (isReceiveMode) R.string.paid_by else R.string.paid_to),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(LocalCustomColors.current.defaultImageCardColor.copy(alpha = 0.3f))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(settlementList) { item ->
                    SettlementRow(item, isReceiveMode)
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

            Column {
                ComposeTextView.TextView(
                    text = item.user.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                ComposeTextView.TextView(
                    text = stringResource(R.string.rupee_formatting, item.amount),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textColor = if (isReceiveMode) LocalCustomColors.current.success else LocalCustomColors.current.error
                )
            }
        }

        if (isReceiveMode) {
            if (!item.isSettled && item.canRequest) {
                ActionButton(
                    text = stringResource(R.string.request),
                    backgroundColor = LocalCustomColors.current.success
                ) {

                }
            } else if (item.isSettled) {
                ComposeTextView.TextView(
                    text = stringResource(R.string.received),
                    fontSize = 16.sp,
                    textColor = LocalCustomColors.current.success,
                    fontWeight = FontWeight.Bold
                )
            } else {
                ComposeTextView.TextView(
                    text = stringResource(R.string.rupee_formatting, item.amount.toInt().toString()),
                    fontSize = 16.sp,
                    textColor = LocalCustomColors.current.success,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            if (!item.isSettled) {
                ActionButton(
                    text = stringResource(R.string.pay),
                    backgroundColor = LocalCustomColors.current.error
                ) {

                }
            } else {
                ComposeTextView.TextView(
                    text = stringResource(R.string.paid),
                    fontSize = 16.sp,
                    textColor = LocalCustomColors.current.textColor.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        ComposeTextView.TextView(
            text = text,
            textColor = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
