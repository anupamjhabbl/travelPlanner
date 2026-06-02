package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.utils.DateUtils.toFormattedDateString
import com.example.bbltripplanner.screens.userTrip.viewModels.TripGalleryViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun TripGalleryImageViewerScreen(
    viewModel: TripGalleryViewModel,
    photoId: String?,
) {
    val photosStatus by viewModel.remotePhotosStatus.collectAsState()
    val photo = photosStatus.data?.find { it.id == photoId }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.primaryBackground)
    ) {
        if (photo != null) {
            ComposeImageView.ImageViewWithUrl(
                imageURI = photo.originalMediaUrl ?: photo.compressedMediaUrl ?: "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp, 24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(LocalCustomColors.current.secondaryBackground.copy(alpha = 0.6f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ComposeImageView.CircularImageView(
                        diameter = 44.dp,
                        imageURI = photo.uploadedBy?.profilePicture ?: ""
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = photo.uploadedBy?.name ?: "Unknown",
                            color = LocalCustomColors.current.primaryBackground,
                            fontSize = 12.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(0.dp),
                            lineHeight = 12.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = photo.createdAt.toFormattedDateString(),
                            color = LocalCustomColors.current.primaryBackground,
                            fontSize = 12.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(0.dp),
                            lineHeight = 12.sp
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = { /* Download logic can be added later */ },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(LocalCustomColors.current.primaryBackground.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = "Download",
                            tint = LocalCustomColors.current.primaryBackground,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    IconButton(
                        onClick = { /* Share logic */ },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(LocalCustomColors.current.success)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = LocalCustomColors.current.primaryBackground,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        } else if (photosStatus.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = LocalCustomColors.current.secondaryBackground)
            }
        }
    }
}
