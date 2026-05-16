package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
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
import com.example.bbltripplanner.screens.userTrip.entity.PhotoUploadStatus
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import com.example.bbltripplanner.screens.userTrip.viewModels.TripGalleryViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch

@Composable
fun TripGalleryScreen(
    viewModel: TripGalleryViewModel
) {
    val scope = rememberCoroutineScope()
    val photos: List<TripPhoto> = listOf(
        TripPhoto("101", "https://picsum.photos/500/300?random=101", status = PhotoUploadStatus.UPLOADING),
        TripPhoto("105", "https://picsum.photos/500/300?random=105"),
        TripPhoto("102", "https://picsum.photos/500/300?random=102", status = PhotoUploadStatus.FAILED),
        TripPhoto("107", "https://picsum.photos/500/300?random=107"),
        TripPhoto("103", "https://picsum.photos/500/300?random=103"),
        TripPhoto("106", "https://picsum.photos/500/300?random=106"),
        TripPhoto("104", "https://picsum.photos/500/300?random=104"),
        TripPhoto("108", "https://picsum.photos/500/300?random=108")
    )


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            GalleryTopBar(tripName = "Paris Summer 2024") {
                scope.launch {
                    CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalCustomColors.current.primaryBackground)
            ) {
                val uploadingCount =
                    photos.count { it.status == PhotoUploadStatus.UPLOADING || it.status == PhotoUploadStatus.PENDING }
                if (uploadingCount > 0) {
                    UploadingStatusHeader(uploadingCount)
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(photos) { photo ->
                        PhotoGridItem(photo) {
                            scope.launch {
                                CommonNavigationChannel.navigateTo(
                                    NavigationAction.Navigate(
                                        AppNavigationScreen.TripGalleryImageViewerScreen.createRoute(photo.id)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomEnd)
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    // open gallery screen to pick photos and also camera option first select what user want
                },
                containerColor = LocalCustomColors.current.secondaryBackground,
                contentColor = LocalCustomColors.current.primaryButtonText,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(bottom = 16.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = LocalCustomColors.current.primaryButtonText
                )

                Spacer(Modifier.width(8.dp))

                ComposeTextView.TextView(
                    text = stringResource(R.string.add_photos),
                    textColor = LocalCustomColors.current.primaryButtonText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun GalleryTopBar(tripName: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = LocalCustomColors.current.secondaryBackground
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            ComposeTextView.TitleTextView(
                text = tripName,
                fontSize = 20.sp,
                textColor = LocalCustomColors.current.secondaryBackground
            )
        }

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = LocalCustomColors.current.secondaryBackground
            )
        }
    }
}

@Composable
fun UploadingStatusHeader(count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(LocalCustomColors.current.warning, CircleShape)
        )

        Spacer(Modifier.width(8.dp))

        ComposeTextView.TextView(
            text = pluralStringResource(R.plurals.uploadingStatus, count, count),
            textColor = LocalCustomColors.current.warning,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun PhotoGridItem(photo: TripPhoto, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        ComposeImageView.ImageViewWithUrl(
            imageURI = photo.localPath ?: photo.url,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (photo.status != PhotoUploadStatus.COMPLETE) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                when (photo.status) {
                    PhotoUploadStatus.UPLOADING, PhotoUploadStatus.PENDING -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }

                    PhotoUploadStatus.FAILED -> {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = Color.White,
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.BottomEnd)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = stringResource(R.string.retry),
                                    tint = LocalCustomColors.current.error,
                                    modifier = Modifier.size(20.dp)
                                )

                                ComposeTextView.TextView(
                                    text = stringResource(R.string.retry),
                                    textColor = LocalCustomColors.current.error,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        // retry to upload photo
                                    }
                                )
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}
