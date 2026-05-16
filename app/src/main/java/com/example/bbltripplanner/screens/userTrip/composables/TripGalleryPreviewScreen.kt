package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.common.composables.ComposeButtonView
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun TripGalleryPreviewScreen(
    onDone: () -> Unit,
    onBack: () -> Unit
) {
    var privacy by remember { mutableStateOf("Public") }
    var allowDownload by remember { mutableStateOf(true) }
    var allowResharing by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Full screen background image
        ComposeImageView.ImageViewWithUrl(
            imageURI = "", // Mock background
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Top Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            Button(
                onClick = onDone,
                colors = ButtonDefaults.buttonColors(containerColor = LocalCustomColors.current.secondaryBackground),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Done", color = Color.White)
            }
        }

        // Bottom Sheet Overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White.copy(alpha = 0.95f))
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ComposeTextView.TitleTextView(text = "Privacy Settings", fontSize = 20.sp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LocalCustomColors.current.error.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Encrypted",
                        color = LocalCustomColors.current.error,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Privacy Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PrivacyOption(
                    icon = Icons.Default.Public,
                    label = "Public",
                    isSelected = privacy == "Public",
                    onClick = { privacy = "Public" },
                    modifier = Modifier.weight(1f)
                )
                PrivacyOption(
                    icon = Icons.Default.Lock,
                    label = "Private",
                    isSelected = privacy == "Private",
                    onClick = { privacy = "Private" },
                    modifier = Modifier.weight(1f)
                )
                PrivacyOption(
                    icon = Icons.Default.Groups,
                    label = "Selected",
                    isSelected = privacy == "Selected",
                    onClick = { privacy = "Selected" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Sharing with 8 people", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Text(
                    text = "Edit List",
                    color = LocalCustomColors.current.error,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(Modifier.height(8.dp))

            // Avatars Row (Simplified)
            Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                repeat(4) {
                    ComposeImageView.CircularImageView(
                        imageURI = "",
                        diameter = 32.dp,
                        borderWidth = 2.dp,
                        borderColor = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(LocalCustomColors.current.error.copy(alpha = 0.2f))
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "+4", fontSize = 12.sp, color = LocalCustomColors.current.error, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(24.dp))

            SettingsToggle(label = "Allow Download", checked = allowDownload, onCheckedChange = { allowDownload = it })
            SettingsToggle(label = "Allow Resharing", checked = allowResharing, onCheckedChange = { allowResharing = it })

            Spacer(Modifier.height(24.dp))

            ComposeButtonView.PrimaryButtonView(
                text = "Share to Gallery",
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                // Share action
            }
        }
    }
}

@Composable
fun PrivacyOption(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) LocalCustomColors.current.secondaryBackground else Color.Transparent
    val contentColor = if (isSelected) Color.White else LocalCustomColors.current.textColor
    val borderColor = if (isSelected) Color.Transparent else Color.LightGray.copy(alpha = 0.5f)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = label, color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SettingsToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.FileDownload,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = LocalCustomColors.current.textColor
            )
            Spacer(Modifier.width(12.dp))
            Text(text = label, fontSize = 16.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = LocalCustomColors.current.secondaryBackground
            )
        )
    }
}
