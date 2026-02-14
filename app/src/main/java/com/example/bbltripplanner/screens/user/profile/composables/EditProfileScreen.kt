package com.example.bbltripplanner.screens.user.profile.composables

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.RequestStatus
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.utils.FileUtils
import com.example.bbltripplanner.common.utils.StringUtils
import com.example.bbltripplanner.common.utils.StringUtils.isValidPhoneNumber
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.user.profile.viewModels.EditProfileIntent
import com.example.bbltripplanner.screens.user.profile.viewModels.EditProfileViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen() {
    val viewModel: EditProfileViewModel = koinViewModel()
    val user = viewModel.getUser()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val defaultMessage = stringResource(R.string.generic_error)
    var imageFile: MultipartBody.Part? by remember { mutableStateOf(null) }
    var imageBitMap: Bitmap? by remember { mutableStateOf(null) }
    var imageUri: Uri? by remember { mutableStateOf(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            when {
                data?.data != null -> {
                    val galleryUri: Uri = data.data!!
                    imageUri = galleryUri
                    imageFile = FileUtils.uriToMultipart(context, galleryUri, Constants.PROFILE_PICTURE_PART_NAME)
                }

                data?.extras?.get("data") != null -> {
                    val cameraBitmap = data.extras?.get("data") as Bitmap
                    imageBitMap = cameraBitmap
                    imageFile = FileUtils.bitMapToMultipart(cameraBitmap, context, Constants.PROFILE_PICTURE_PART_NAME)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.userUpdateStatus.collectLatest { state ->
            when (state) {
                RequestStatus.Idle -> { isLoading = false }

                RequestStatus.Loading -> { isLoading = true }

                is RequestStatus.Error -> {
                    isLoading = false
                    if (state.message == Constants.DEFAULT_ERROR) {
                        ComposeViewUtils.showToast(context, defaultMessage)
                    } else {
                        ComposeViewUtils.showToast(context, state.message ?: "")
                    }
                }

                is RequestStatus.Success<String> -> {
                    isLoading = false
                    ComposeViewUtils.showToast(context, state.data)
                    CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                }
            }
        }
    }

    if (user == null) {
        ComposeViewUtils.FullScreenErrorComposable(
            Pair(stringResource(R.string.inconvenience_sorry),
                stringResource(R.string.restart_app_message)
            )
        )
    } else if (isLoading) {
        ComposeViewUtils.Loading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalCustomColors.current.primaryBackground)
        ) {
            EditProfileToolbar(context, user.id)

            Column {
                Spacer(Modifier.height(60.dp))

                ProfileImageSection(
                    profilePicture = user.profilePicture,
                    imageUri = imageUri,
                    imageBitMap = imageBitMap,
                    onChangePictureClick = { updateProfilePic(imageLauncher) }
                )
            }

            UserFieldsSection(
                user = user
            ) { updatedUser ->
                viewModel.processEvent(EditProfileIntent.ViewEvent.UpdateUser(updatedUser, imageFile))
            }
        }
    }
}

private fun updateProfilePic(
    imageLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val chooserIntent = Intent.createChooser(pickIntent, null)
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
    imageLauncher.launch(chooserIntent)
}


private fun shareProfile(
    context: Context,
    userId: String
) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, StringUtils.getDeeplinkForUserShare(userId))
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Composable
private fun EditProfileToolbar(
    context: Context,
    userId: String
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                }
            },
            modifier = Modifier.size(36.dp),
            colors = IconButtonDefaults.iconButtonColors().copy(
                containerColor = LocalCustomColors.current.secondaryBackground,
                contentColor = LocalCustomColors.current.primaryBackground
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(28.dp),
                tint = LocalCustomColors.current.primaryBackground
            )
        }

        ComposeTextView.TitleTextView(
            text = stringResource(R.string.edit_profile)
        )

        IconButton(
            onClick = { shareProfile(context, userId) },
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                modifier = Modifier.size(28.dp),
                tint = LocalCustomColors.current.secondaryBackground
            )
        }
    }
}

@Composable
private fun ProfileImageSection(
    profilePicture: String?,
    imageUri: Uri?,
    imageBitMap: Bitmap?,
    onChangePictureClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(120.dp)
                .border(2.dp, LocalCustomColors.current.secondaryBackground.copy(0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (imageBitMap != null) {
                ComposeImageView.CircularImageView(
                    diameter = 110.dp,
                    bitmap = imageBitMap.asImageBitmap()
                )
            }  else if (imageUri != null) {
                ComposeImageView.CircularImageView(
                    diameter = 110.dp,
                    imageURI = imageUri
                )
            } else {
                ComposeImageView.CircularImageView(
                    diameter = 110.dp,
                    imageURI = profilePicture ?: ""
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        ComposeTextView.TextView(
            text = stringResource(R.string.change_profile_picture),
            textColor = LocalCustomColors.current.secondaryBackground,
            fontSize = 14.sp,
            modifier = Modifier.clickable {
                onChangePictureClick()
            }
        )
    }
}

@Composable
private fun UserFieldsSection(
    user: User,
    onUpdateClick: (user: User) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val maxBioLength = 100
        var username by remember { mutableStateOf(user.name) }
        var bio by remember { mutableStateOf(user.bio) }
        var countryCode by remember { mutableStateOf(user.phone?.take(3) ?: "+91") }
        var phoneNumber by remember { mutableStateOf(user.phone?.drop(3) ?: "") }
        var hasPhoneFocused by remember { mutableStateOf(false) }
        var isPhoneFocused by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = {
                ComposeTextView.TextView(
                    stringResource(R.string.username),
                    textColor = LocalCustomColors.current.secondaryBackground
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                ComposeTextView.TextView(
                    text = stringResource(R.string.username_hint),
                    fontSize = 16.sp,
                    textColor = LocalCustomColors.current.hintTextColor,
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedTextColor = LocalCustomColors.current.textColor,
                focusedIndicatorColor = LocalCustomColors.current.secondaryBackground,
                unfocusedTextColor = LocalCustomColors.current.textColor,
                errorIndicatorColor = LocalCustomColors.current.error
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = bio ?: "",
            onValueChange = {
                if (it.length <= maxBioLength) {
                    bio = it
                }
            },
            label = {
                ComposeTextView.TextView(
                    stringResource(R.string.bio),
                    textColor = LocalCustomColors.current.secondaryBackground
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                ComposeTextView.TextView(
                    text = stringResource(R.string.username_hint),
                    fontSize = 16.sp,
                    textColor = LocalCustomColors.current.hintTextColor,
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedTextColor = LocalCustomColors.current.textColor,
                focusedIndicatorColor = LocalCustomColors.current.secondaryBackground,
                unfocusedTextColor = LocalCustomColors.current.textColor,
                errorIndicatorColor = LocalCustomColors.current.error
            )
        )

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 0.dp)) {
            ComposeTextView.TextView(
                text = "${bio?.length ?: 0}/$maxBioLength",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            OutlinedTextField(
                value = countryCode,
                onValueChange = { cc ->
                    if (cc.length <= 3 && cc.isNotEmpty() && cc.startsWith("+")) {
                        countryCode = cc
                    } else if (cc.isEmpty()) {
                        countryCode = "+"
                    }
                },
                label = {
                    ComposeTextView.TextView(
                        stringResource(R.string.code),
                        textColor = LocalCustomColors.current.secondaryBackground
                    )
                },
                modifier = Modifier
                    .weight(0.2f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            hasPhoneFocused = true
                        }
                        isPhoneFocused = focusState.isFocused
                    },
                placeholder = {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.country_code_hint),
                        fontSize = 16.sp,
                        textColor = LocalCustomColors.current.hintTextColor,
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedTextColor = LocalCustomColors.current.textColor,
                    focusedIndicatorColor = LocalCustomColors.current.secondaryBackground,
                    unfocusedTextColor = LocalCustomColors.current.textColor,
                    errorIndicatorColor = LocalCustomColors.current.error
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it.filter { char -> char.isDigit() } },
                label = {
                    ComposeTextView.TextView(
                        stringResource(R.string.phone),
                        textColor = LocalCustomColors.current.secondaryBackground
                    )
                },
                modifier = Modifier
                    .weight(0.8f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            hasPhoneFocused = true
                        }
                        isPhoneFocused = focusState.isFocused
                    },
                placeholder = {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.phone_hint),
                        fontSize = 16.sp,
                        textColor = LocalCustomColors.current.hintTextColor,
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedTextColor = LocalCustomColors.current.textColor,
                    focusedIndicatorColor = LocalCustomColors.current.secondaryBackground,
                    unfocusedTextColor = LocalCustomColors.current.textColor,
                    errorIndicatorColor = LocalCustomColors.current.error
                ),
                isError = !isPhoneFocused && !(countryCode + phoneNumber).isValidPhoneNumber() && hasPhoneFocused,
                supportingText = {
                    if (!isPhoneFocused && !(countryCode + phoneNumber).isValidPhoneNumber() && hasPhoneFocused) {
                        ComposeTextView.TextView(
                            text = stringResource(R.string.invalid_phone_alert),
                            textColor = LocalCustomColors.current.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val fullPhoneNumber = if (phoneNumber.isNotEmpty()) countryCode + phoneNumber else ""
                onUpdateClick(
                    user.copy(
                        name = username,
                        bio =  bio,
                        phone = fullPhoneNumber
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(0.5f),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalCustomColors.current.secondaryBackground,
                contentColor = LocalCustomColors.current.primaryBackground
            ),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(16.dp, 16.dp)
        ) {
            ComposeTextView.TitleTextView(
                text = stringResource(R.string.update),
                textColor = LocalCustomColors.current.primaryBackground,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
