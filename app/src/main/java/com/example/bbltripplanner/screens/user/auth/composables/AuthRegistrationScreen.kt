package com.example.bbltripplanner.screens.user.auth.composables

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.bbltripplanner.screens.user.auth.viewModels.UseRegistrationViewModel
import com.example.bbltripplanner.screens.user.auth.viewModels.UserAuthIntent
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthRegistrationScreen(
    navController: NavController,
    onLoginClick: () -> Unit,
    onAppleLoginClick: () -> Unit,
    onGoogleLoginClick:  () -> Unit
) {
    val context = LocalContext.current
    val genericMessage = stringResource(R.string.generic_error)
    val viewModel: UseRegistrationViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.userRegisterRequestStatus.collectLatest { authRegistrationRequestStatus ->
            when (authRegistrationRequestStatus) {
                is RequestStatus.Error -> {
                    isLoading = false
                    if ((authRegistrationRequestStatus as? RequestStatus.Error)?.message == Constants.DEFAULT_ERROR) {
                        showToast(context, genericMessage)
                    } else {
                        showToast(
                            context,
                            (authRegistrationRequestStatus as? RequestStatus.Error)?.message ?: ""
                        )
                    }
                }

                RequestStatus.Loading -> {
                    isLoading = true
                }

                is RequestStatus.Success -> {
                    isLoading = false
                    navController.navigate(
                        AppNavigationScreen.OtpVerificationScreen.createRoute(
                            userEmail = state.email,
                            Constants.Origin.REGISTRATION,
                            (authRegistrationRequestStatus as? RequestStatus.Success)?.data?.userId
                                ?: ""
                        )
                    )
                }

                RequestStatus.Idle -> { isLoading = false }
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
                .padding(8.dp, 0.dp)
                .verticalScroll(scrollState)
        ) {
            ComposeTextView.TextView(
                text = stringResource(R.string.username),
                textColor = LocalCustomColors.current.textColor,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = state.userName,
                onValueChange = {
                    viewModel.processEvent(
                        UserAuthIntent.RegisterAuth.ViewEvent.UpdateUsername(
                            it
                        )
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
                    unfocusedTextColor = LocalCustomColors.current.textColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            ComposeTextView.TextView(
                text = stringResource(R.string.email),
                textColor = LocalCustomColors.current.textColor,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = {
                    viewModel.processEvent(UserAuthIntent.RegisterAuth.ViewEvent.UpdateEmail(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.email_hint),
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
                isError = !state.emailValid && state.email.isNotEmpty(),
                supportingText = {
                    if (!state.emailValid && state.email.isNotEmpty()) {
                        ComposeTextView.TextView(
                            text = stringResource(R.string.invalid_email_alert),
                            textColor = LocalCustomColors.current.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ComposeTextView.TextView(
                text = stringResource(R.string.password),
                textColor = LocalCustomColors.current.textColor,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = {
                    viewModel.processEvent(
                        UserAuthIntent.RegisterAuth.ViewEvent.UpdatePassword(
                            it
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    ComposeTextView.TextView(
                        text = stringResource(R.string.password_hint),
                        fontSize = 16.sp,
                        textColor = LocalCustomColors.current.hintTextColor
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
                isError = state.passwordValid != PasswordStrengthValidityStatus.VALID && state.passwordValid != null,
                supportingText = {
                    if (state.passwordValid != PasswordStrengthValidityStatus.VALID && state.passwordValid != null) {
                        ComposeTextView.TextView(
                            text = state.passwordValid?.message ?: "",
                            textColor = LocalCustomColors.current.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.processEvent(UserAuthIntent.RegisterAuth.ViewEvent.RegisterUser)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalCustomColors.current.secondaryBackground,
                    disabledContainerColor = LocalCustomColors.current.fadedBackground
                ),
                enabled = state.passwordValid == PasswordStrengthValidityStatus.VALID && state.emailValid && state.userNameValid
            ) {
                ComposeTextView.TitleTextView(
                    stringResource(R.string.start_your_jorney),
                    textColor = LocalCustomColors.current.primaryBackground,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(1.dp).weight(1f).background(LocalCustomColors.current.hintTextColor))
                ComposeTextView.TextView(
                    stringResource(R.string.auth_or),
                    textColor = LocalCustomColors.current.hintTextColor,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(1.dp).weight(1f).background(LocalCustomColors.current.hintTextColor))
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onGoogleLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, color = LocalCustomColors.current.hintTextColor)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                ComposeTextView.TextView(
                    text = stringResource(R.string.register_with_google),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onAppleLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, color = LocalCustomColors.current.hintTextColor)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_apple),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                ComposeTextView.TextView(
                    text = stringResource(R.string.register_with_apple),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ComposeTextView.TextView(
                    text = stringResource(R.string.already_have_an_account),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { onLoginClick() }
                )

                Spacer(Modifier.width(2.dp))

                ComposeTextView.TextView(
                    text = stringResource(R.string.login),
                    textColor = LocalCustomColors.current.secondaryBackground,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}