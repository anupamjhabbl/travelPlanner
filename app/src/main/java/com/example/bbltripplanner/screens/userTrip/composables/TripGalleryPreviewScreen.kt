package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.AttachmentPrivacy
import com.example.bbltripplanner.screens.userTrip.entity.PhotoUploadStatus
import com.example.bbltripplanner.screens.userTrip.entity.TripPhoto
import com.example.bbltripplanner.screens.userTrip.viewModels.TripGalleryViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripGalleryPreviewScreen(
    viewModel: TripGalleryViewModel
) {
    val images: List<TripPhoto> = listOf(
        TripPhoto("101", "https://picsum.photos/500/300?random=101", status = PhotoUploadStatus.UPLOADING),
        TripPhoto("105", "https://picsum.photos/500/300?random=105"),
        TripPhoto("102", "https://picsum.photos/500/300?random=102", status = PhotoUploadStatus.FAILED),
        TripPhoto("107", "https://picsum.photos/500/300?random=107"),
        TripPhoto("103", "https://picsum.photos/500/300?random=103"),
        TripPhoto("106", "https://picsum.photos/500/300?random=106"),
        TripPhoto("104", "https://picsum.photos/500/300?random=104"),
        TripPhoto("108", "https://picsum.photos/500/300?random=108")
    )
    val peopleList = listOf(
        User(
            "1",
            "Alex Rivera",
            phone = null,
            email = null,
            bio = null,
            profilePicture = "https://picsum.photos/400/200",
            userStory = null,
        ),
        User(
            "2",
            "Alex Rivera",
            phone = null,
            email = null,
            bio = null,
            profilePicture = "https://picsum.photos/400/200",
            userStory = null,
        ),
        User(
            "3",
            "Alex Rivera",
            phone = null,
            email = null,
            bio = null,
            profilePicture = "https://picsum.photos/400/200",
            userStory = null,
        ),
        User(
            "4",
            "Alex Rivera",
            phone = null,
            email = null,
            bio = null,
            profilePicture = "https://picsum.photos/400/200",
            userStory = null,
        )
    )
    val selectedPeopleList = listOf(
        User(
            "1",
            "Alex Rivera",
            phone = null,
            email = null,
            bio = null,
            profilePicture = "https://picsum.photos/400/200",
            userStory = null,
        ),
        User(
            "2",
            "Alex Rivera",
            phone = null,
            email = null,
            bio = null,
            profilePicture = "https://picsum.photos/400/200",
            userStory = null,
        )
    )
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
            skipHiddenState = true
        )
    )

    var selectedImageIndex by remember { mutableIntStateOf(0) }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 180.dp,
        sheetContainerColor = LocalCustomColors.current.primaryBackground.copy(alpha = 0.9f),
        sheetContent = {
            BottomSheetContent(
                images = images,
                peopleList = peopleList,
                selectedPeopleList = selectedPeopleList,
                selectedImageIndex = selectedImageIndex,
                onImageSelected = { selectedImageIndex = it }
            )
        },
        sheetShadowElevation = 16.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ComposeImageView.ImageViewWithUrl(
                imageURI = images[selectedImageIndex].url,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            PreviewToolbar(
                onClose = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                    }
                },
                onDone = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                    }
                }
            )
        }
    }
}

@Composable
fun PreviewToolbar(
    onClose: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(LocalCustomColors.current.secondaryBackground)
                .clickable {
                    onClose()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = LocalCustomColors.current.primaryBackground
            )
        }

        Button(
            onClick = onDone,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalCustomColors.current.secondaryBackground
            ),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.done),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BottomSheetContent(
    images: List<TripPhoto>,
    peopleList: List<User>,
    selectedPeopleList: List<User>,
    selectedImageIndex: Int,
    onImageSelected: (Int) -> Unit
) {
    var privacy: AttachmentPrivacy by remember { mutableStateOf(AttachmentPrivacy.PUBLIC) }
    var allowDownload by remember { mutableStateOf(true) }
    var allowResharing by remember { mutableStateOf(false) }
    val horizontalScrollState = rememberScrollState()
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .padding(start = 20.dp, end = 20.dp, bottom = 16.dp, top = 0.dp)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(images) { index, image ->
                val isSelected = index == selectedImageIndex
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = if (isSelected)
                                LocalCustomColors.current.secondaryBackground
                            else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onImageSelected(index) }
                ) {
                    ComposeImageView.ImageViewWithUrl(
                        imageURI = image.url,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ComposeTextView.TitleTextView(
                text = stringResource(R.string.privacy_settings),
                fontSize = 20.sp
            )

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = LocalCustomColors.current.error,
                modifier = Modifier.clickable {
                    // delete the photo
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PrivacyOption(Icons.Default.Public, AttachmentPrivacy.PUBLIC.value, privacy == AttachmentPrivacy.PUBLIC) {
                isEditing = false
                privacy = AttachmentPrivacy.PUBLIC
            }
            PrivacyOption(Icons.Default.Lock, AttachmentPrivacy.PRIVATE.value, privacy == AttachmentPrivacy.PRIVATE) {
                isEditing = false
                privacy = AttachmentPrivacy.PRIVATE
            }
            PrivacyOption(Icons.Default.Groups, AttachmentPrivacy.SELECTED.value, privacy == AttachmentPrivacy.SELECTED) {
                isEditing = false
                privacy = AttachmentPrivacy.SELECTED
            }
        }

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(LocalCustomColors.current.secondaryBackground.copy(alpha = 0.05f))
                .padding(8.dp, 12.dp),
        ) {
            Row {
                Text(
                    text = stringResource(R.string.sharing_with, 8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )

                if (privacy == AttachmentPrivacy.SELECTED) {
                    val text = if (isEditing) stringResource(R.string.done) else stringResource(R.string.edit_list)
                    Text(
                        text = text,
                        color = LocalCustomColors.current.warning,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            isEditing = !isEditing
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            if (isEditing) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    peopleList.forEach { user ->
                        val borderColor = if (selectedPeopleList.find { it.id == user.id } != null) LocalCustomColors.current.secondaryBackground else Color.Transparent
                        AssistChip(
                            onClick = {},
                            label = { ComposeTextView.TextView(user.name) },
                            leadingIcon = {
                                ComposeImageView.CircularImageView(
                                    diameter = 24.dp,
                                    imageURI = user.profilePicture ?: ""
                                )
                            },
                            shape = RoundedCornerShape(40),
                            border = BorderStroke(1.dp, color = borderColor)
                        )
                    }

                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy((-8).dp),
                    modifier = Modifier.horizontalScroll(horizontalScrollState)
                ) {
                    selectedPeopleList.forEach { people ->
                        ComposeImageView.CircularImageView(
                            imageURI = "https://picsum.photos/400/200?1",
                            diameter = 32.dp,
                            borderWidth = 2.dp,
                            borderColor = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier.size(32.dp)
                            .clip(CircleShape)
                            .background(
                                LocalCustomColors.current.secondaryBackground.copy(alpha = 0.2f)
                            )
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+4",
                            fontSize = 12.sp,
                            color = LocalCustomColors.current.secondaryBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        SettingsToggle(stringResource(R.string.allow_downlaod), allowDownload) { allowDownload = it }

        SettingsToggle(stringResource(R.string.allow_sharing), allowResharing) { allowResharing = it }
    }
}

@Composable fun PrivacyOption(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
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
            .padding(vertical = 12.dp, horizontal = 8.dp),
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

        Text(
            text = label,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SettingsToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
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