package com.example.bbltripplanner.screens.user.auth.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.RequestStatus
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.user.auth.entity.PasswordStrengthValidityStatus
import com.example.bbltripplanner.screens.user.auth.viewModels.PasswordResetVieModel
import com.example.bbltripplanner.screens.user.auth.viewModels.UserAuthIntent
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun PasswordResetScreen() {
    val context = LocalContext.current
    val genericMessage = stringResource(R.string.generic_error)
    val viewModel: PasswordResetVieModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var hasPasswordFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var hasCPasswordFocused by remember { mutableStateOf(false) }
    var isCPasswordFocused by remember { mutableStateOf(false) }
    var isLoading  by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.userResetPasswordRequestStatus.collectLatest { resetPasswordRequestStatus ->
            when (resetPasswordRequestStatus) {
                is RequestStatus.Error -> {
                    isLoading = false
                    if (resetPasswordRequestStatus.message == Constants.DEFAULT_ERROR) {
                        ComposeViewUtils.showToast(context, genericMessage)
                    } else {
                        ComposeViewUtils.showToast(
                            context,
                            resetPasswordRequestStatus.message ?: ""
                        )
                    }
                }

                RequestStatus.Idle -> { isLoading = false }

                RequestStatus.Loading -> { isLoading = true }

                is RequestStatus.Success -> {
                    isLoading = false
                    CommonNavigationChannel.navigateTo(
                        NavigationAction.Navigate(
                            AppNavigationScreen.HomeScreen.route
                        ) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    )
                }
            }
        }
    }

    if (isLoading) {
        Box(
            Modifier.fillMaxSize()
        ) {
            ComposeViewUtils.Loading(
                modifier = Modifier.fillMaxSize().wrapContentSize()
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(LocalCustomColors.current.secondaryBackground, CircleShape)
                    .size(32.dp)
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            CommonNavigationChannel.navigateTo(
                                NavigationAction.Navigate(
                                    AppNavigationScreen.HomeScreen.route
                                )
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = LocalCustomColors.current.primaryBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            ComposeTextView.TitleTextView(
                text = stringResource(R.string.new_password_title)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ComposeTextView.TextView(
                text = stringResource(R.string.new_password_subtitle),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            ComposeTextView.TextView(
                text = stringResource(R.string.password),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = {
                    viewModel.processEvent(
                        UserAuthIntent.ResetPasswordAuth.ViewEvent.UpdatePassword(
                            it
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            hasPasswordFocused = true
                        }
                        isPasswordFocused = focusState.isFocused
                    },
                placeholder = {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.new_password_hint),
                        textColor = LocalCustomColors.current.hintTextColor,
                        fontSize = 16.sp
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = if (passwordVisible) painterResource(R.drawable.ic_visibility_on) else painterResource(
                                R.drawable.ic_visibility_off
                            ),
                            contentDescription = null
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedTextColor = LocalCustomColors.current.textColor,
                    focusedIndicatorColor = LocalCustomColors.current.secondaryBackground,
                    unfocusedTextColor = LocalCustomColors.current.textColor,
                    errorIndicatorColor = LocalCustomColors.current.error
                ),
                isError = state.passwordValid != PasswordStrengthValidityStatus.VALID && !isPasswordFocused && hasPasswordFocused,
                supportingText = {
                    if (state.passwordValid != PasswordStrengthValidityStatus.VALID && !isPasswordFocused && hasPasswordFocused) {
                        ComposeTextView.TextView(
                            text = state.passwordValid?.message ?: stringResource(R.string.password_not_empty),
                            textColor = LocalCustomColors.current.error
                        )
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            ComposeTextView.TextView(
                text = stringResource(R.string.confirm_password),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = {
                    viewModel.processEvent(
                        UserAuthIntent.ResetPasswordAuth.ViewEvent.UpdateConfirmPassword(
                            it
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            hasCPasswordFocused = true
                        }
                        isCPasswordFocused = focusState.isFocused
                    },
                placeholder = {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.confirm_password_hint),
                        textColor = LocalCustomColors.current.hintTextColor,
                        fontSize = 16.sp
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { confirmPasswordVisible = !confirmPasswordVisible },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = if (confirmPasswordVisible) painterResource(R.drawable.ic_visibility_on) else painterResource(
                                R.drawable.ic_visibility_off
                            ),
                            contentDescription = null
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedTextColor = LocalCustomColors.current.textColor,
                    focusedIndicatorColor = LocalCustomColors.current.secondaryBackground,
                    unfocusedTextColor = LocalCustomColors.current.textColor,
                    errorIndicatorColor =  LocalCustomColors.current.error
                ),
                isError = !state.confirmPasswordValid && !isCPasswordFocused && hasCPasswordFocused,
                supportingText = {
                    if (!state.confirmPasswordValid && !isCPasswordFocused && hasCPasswordFocused) {
                        ComposeTextView.TextView(
                            text = stringResource(R.string.both_password_not_matching_alert),
                            textColor = LocalCustomColors.current.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.processEvent(UserAuthIntent.ResetPasswordAuth.ViewEvent.ResetPassword)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = state.confirmPasswordValid && state.passwordValid == PasswordStrengthValidityStatus.VALID,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalCustomColors.current.secondaryBackground,
                    contentColor = LocalCustomColors.current.primaryBackground,
                    disabledContainerColor = LocalCustomColors.current.fadedBackground,
                    disabledContentColor = LocalCustomColors.current.defaultImageCardColor
                )
            ) {
                ComposeTextView.TitleTextView(
                    text = stringResource(R.string.reset_password),
                    textColor = LocalCustomColors.current.primaryBackground,
                    fontSize = 16.sp
                )
            }
        }
    }
}