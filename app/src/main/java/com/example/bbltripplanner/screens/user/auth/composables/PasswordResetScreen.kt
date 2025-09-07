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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.RequestStatus
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.screens.posting.composables.showToast
import com.example.bbltripplanner.screens.user.auth.entity.PasswordStrengthValidityStatus
import com.example.bbltripplanner.screens.user.auth.viewModels.PasswordResetVieModel
import com.example.bbltripplanner.screens.user.auth.viewModels.UserAuthIntent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun PasswordResetScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val genericMessage = stringResource(R.string.generic_error)
    val viewModel: PasswordResetVieModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading  by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.userResetPasswordRequestStatus.collectLatest { resetPasswordRequestStatus ->
            when (resetPasswordRequestStatus) {
                is RequestStatus.Error -> {
                    isLoading = false
                    if ((resetPasswordRequestStatus as? RequestStatus.Error)?.message == Constants.DEFAULT_ERROR) {
                        showToast(context, genericMessage)
                    } else {
                        showToast(
                            context,
                            (resetPasswordRequestStatus as? RequestStatus.Error)?.message ?: ""
                        )
                    }
                }

                RequestStatus.Idle -> { isLoading = false }

                RequestStatus.Loading -> { isLoading = true }

                is RequestStatus.Success -> {
                    isLoading = false
                    navController.navigate(AppNavigationScreen.HomeScreen.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
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
                    .background(colorResource(R.color.primary), CircleShape)
                    .size(32.dp)
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(AppNavigationScreen.HomeScreen.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = colorResource(R.color.white)
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
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.new_password_hint),
                        textColor = colorResource(R.color.input_hint_color),
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
                    focusedTextColor = colorResource(R.color.textSecondary),
                    focusedIndicatorColor = colorResource(R.color.primary),
                    unfocusedTextColor = colorResource(R.color.textSecondary),
                    errorIndicatorColor = colorResource(R.color.error_red)
                ),
                isError = state.passwordValid != null && state.passwordValid != PasswordStrengthValidityStatus.VALID,
                supportingText = {
                    if (state.passwordValid != null && state.passwordValid != PasswordStrengthValidityStatus.VALID) {
                        ComposeTextView.TextView(
                            text = state.passwordValid?.message ?: "",
                            textColor = colorResource(R.color.error_red)
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
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.confirm_password_hint),
                        textColor = colorResource(R.color.input_hint_color),
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
                    focusedTextColor = colorResource(R.color.textSecondary),
                    focusedIndicatorColor = colorResource(R.color.primary),
                    unfocusedTextColor = colorResource(R.color.textSecondary),
                    errorIndicatorColor = colorResource(R.color.error_red)
                ),
                isError = !state.confirmPasswordValid,
                supportingText = {
                    if (!state.confirmPasswordValid) {
                        ComposeTextView.TextView(
                            text = stringResource(R.string.both_password_not_matching_alert),
                            textColor = colorResource(R.color.error_red)
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
                    containerColor = colorResource(R.color.primary),
                    contentColor = colorResource(R.color.white),
                    disabledContainerColor = colorResource(R.color.faded_primary),
                    disabledContentColor = colorResource(R.color.bg_default_image)
                )
            ) {
                ComposeTextView.TitleTextView(
                    text = stringResource(R.string.reset_password),
                    textColor = colorResource(R.color.white),
                    fontSize = 16.sp
                )
            }
        }
    }
}