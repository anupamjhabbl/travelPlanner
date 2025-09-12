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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.example.bbltripplanner.screens.user.auth.viewModels.ForgotPasswordAuthViewModel
import com.example.bbltripplanner.screens.user.auth.viewModels.UserAuthIntent
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val genericMessage = stringResource(R.string.generic_error)
    val viewModel: ForgotPasswordAuthViewModel =  koinViewModel()
    val state by viewModel.state.collectAsState()
    var isEmailFocused by remember { mutableStateOf(false) }
    var hasEmailFocused by remember { mutableStateOf(false) }
    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.userForgetPasswordRequestStatus.collectLatest { forgotPasswordRequestStatus ->
            when (forgotPasswordRequestStatus) {
                is RequestStatus.Error -> {
                    isLoading = false
                    if ((forgotPasswordRequestStatus as? RequestStatus.Error)?.message == Constants.DEFAULT_ERROR) {
                        showToast(context, genericMessage)
                    } else {
                        showToast(
                            context,
                            (forgotPasswordRequestStatus as? RequestStatus.Error)?.message ?: ""
                        )
                    }
                }
                RequestStatus.Idle -> { isLoading = false }

                RequestStatus.Loading -> { isLoading = true }

                is RequestStatus.Success -> {
                    isLoading = false
                    navController.navigate(
                        AppNavigationScreen.OtpVerificationScreen.createRoute(
                            state.email,
                            Constants.Origin.FORGOT_PASSWORD,
                            (forgotPasswordRequestStatus as? RequestStatus.Success)?.data?.userId
                                ?: ""
                        )
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
                        navController.popBackStack()
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
                text = stringResource(R.string.forgot_password)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ComposeTextView.TextView(
                text = stringResource(R.string.forgot_password_subtitle),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            ComposeTextView.TextView(
                text = stringResource(R.string.email),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = {
                    viewModel.processEvent(
                        UserAuthIntent.ForgetPasswordAuth.ViewEvent.UpdateEmail(
                            it
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            hasEmailFocused = true
                        }
                        isEmailFocused = focusState.isFocused
                    },
                placeholder = {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.email_hint),
                        textColor = LocalCustomColors.current.hintTextColor,
                        fontSize = 16.sp
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
                isError = !state.isValid && hasEmailFocused && !isEmailFocused,
                supportingText = {
                    if (!state.isValid && hasEmailFocused && !isEmailFocused) {
                        ComposeTextView.TextView(
                            text = stringResource(R.string.invalid_email_alert),
                            textColor = LocalCustomColors.current.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.processEvent(UserAuthIntent.ForgetPasswordAuth.ViewEvent.ResetPassword)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = state.isValid,
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

