package com.example.bbltripplanner.screens.vault.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ToolBarView
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch

@Composable
fun UserVaultScreen() {
    val customColors = LocalCustomColors.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.primaryBackground)
    ) {
        ToolBarView.SimpleToolbarWithBackButton(title = "Travel Vault") {
            scope.launch {
                CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats section banner (rich layout)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            customColors.secondaryBackground,
                            customColors.secondaryBackground.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                ComposeTextView.TextView(
                    text = "Welcome to your Vault",
                    textColor = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                ComposeTextView.TitleTextView(
                    text = "Your Travel Statistics",
                    textColor = Color.White,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem(number = "12", label = "Trips", tintColor = Color.White)
                    StatItem(number = "8", label = "Favourites", tintColor = Color.White)
                    StatItem(number = "34", label = "Stories", tintColor = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        ComposeTextView.TitleTextView(
            text = "Manage your journeys",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 18.sp,
            textColor = customColors.titleTextColor
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Menu items
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VaultMenuCard(
                icon = Icons.Default.FlightTakeoff,
                title = "Your Trips",
                subtitle = "Manage your active itineraries, maps, and schedules",
                onClick = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(
                                AppNavigationScreen.UserTripsScreen.route
                            )
                        )
                    }
                }
            )

            VaultMenuCard(
                icon = Icons.Default.Forum,
                title = "Travel Buzz",
                subtitle = "Share experiences and read social threads from mates",
                onClick = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(
                                AppNavigationScreen.BuzzScreen.route
                            )
                        )
                    }
                }
            )

            VaultMenuCard(
                icon = Icons.Default.Settings,
                title = "Preferences",
                subtitle = "Configure system preferences and support",
                onClick = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(
                                AppNavigationScreen.UserSettingsScreen.route
                            )
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun StatItem(number: String, label: String, tintColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        ComposeTextView.TextView(
            text = number,
            textColor = tintColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        ComposeTextView.TextView(
            text = label,
            textColor = tintColor.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun VaultMenuCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(customColors.fadedBackground.copy(alpha = 0.4f))
            .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(customColors.secondaryBackground.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = customColors.secondaryBackground,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            ComposeTextView.TextView(
                text = title,
                textColor = customColors.titleTextColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            ComposeTextView.TextView(
                text = subtitle,
                textColor = customColors.hintTextColor,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = customColors.secondaryBackground,
            modifier = Modifier.size(20.dp)
        )
    }
}
