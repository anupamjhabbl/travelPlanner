package com.example.bbltripplanner.screens.user.auth.composables

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.entity.RequestStatus
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.screens.posting.composables.showToast
import com.example.bbltripplanner.screens.user.auth.entity.OTPAction
import com.example.bbltripplanner.screens.user.auth.entity.OTPState
import com.example.bbltripplanner.screens.user.auth.viewModels.OTPAuthViewModel
import com.example.bbltripplanner.screens.user.auth.viewModels.UserAuthIntent
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OTPVerificationScreen(
    navController: NavController,
    userEmail: String?,
    origin: String?,
    userId: String?
) {
    val context = LocalContext.current
    val genericMessage = stringResource(R.string.generic_error)
    val  otpResendSuccessMessage = stringResource(R.string.otp_resend_success_message)
    val viewModel: OTPAuthViewModel = koinViewModel()
    val otpState by viewModel.otpState.collectAsState()
    val focusRequesters = remember {
        List(viewModel.getOTPLength()) { FocusRequester() }
    }
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current
    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(otpState.focusedIndex) {
        otpState.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(otpState.code, keyboardManager) {
        val allNumbersEntered = otpState.code.none { it == null }
        if(allNumbersEntered) {
            focusRequesters.forEach {
                it.freeFocus()
            }
            focusManager.clearFocus()
            keyboardManager?.hide()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.userOTPResendRequestStatus.collectLatest { otpResendRequestStatus ->
            when (otpResendRequestStatus) {
                is RequestStatus.Error -> {
                    isLoading = false
                    if (otpResendRequestStatus.message == Constants.DEFAULT_ERROR) {
                        showToast(context, genericMessage)
                    } else {
                        showToast(
                            context,
                            otpResendRequestStatus.message ?: ""
                        )
                    }
                }

                RequestStatus.Idle -> {
                    isLoading = false
                }

                RequestStatus.Loading -> {
                    isLoading = true
                }

                is RequestStatus.Success -> {
                    isLoading = false
                    if (userEmail != null && origin != null) {
                        viewModel.processEvent(
                            UserAuthIntent.OTPAuth.ViewEvent.SetData(
                                userEmail,
                                otpResendRequestStatus.data,
                                origin
                            )
                        )
                    }
                    showToast(context, otpResendSuccessMessage)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (userEmail != null && userId != null && origin != null) {
            viewModel.processEvent(UserAuthIntent.OTPAuth.ViewEvent.SetData(userEmail, userId, origin))
        }
        launch {
            viewModel.userOTPVerifyRequestStatus.collectLatest { otpVerifyRequestStatus ->
                when (otpVerifyRequestStatus) {
                    is RequestStatus.Error -> {
                        isLoading = false
                        if (otpVerifyRequestStatus.message == Constants.DEFAULT_ERROR) {
                            showToast(context, genericMessage)
                        } else {
                            showToast(
                                context,
                                otpVerifyRequestStatus.message ?: ""
                            )
                        }
                    }

                    RequestStatus.Idle -> {
                        isLoading = false
                    }

                    RequestStatus.Loading -> {
                        isLoading = true
                    }

                    is RequestStatus.Success -> {
                        isLoading = false
                        val route = if (origin == Constants.Origin.FORGOT_PASSWORD) {
                            AppNavigationScreen.ResetPasswordScreen.route
                        } else {
                            AppNavigationScreen.HomeScreen.route
                        }
                        navController.navigate(route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }

    if (userEmail == null || isLoading) {
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
                .padding(horizontal = 24.dp, vertical = 32.dp)
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

            Spacer(modifier = Modifier.height(24.dp))

            ComposeTextView.TitleTextView(
                text = stringResource(R.string.check_your_email)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ComposeTextView.TextView(
                text = stringResource(
                    R.string.check_your_email_subtitle,
                    userEmail,
                    viewModel.getOTPLength()
                ),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OTPVerificationTextField(
                otpState,
                focusRequesters,
                onAction = { action ->
                    when (action) {
                        is OTPAction.OnEnterNumber -> {
                            if (action.number != null) {
                                focusRequesters[action.index].freeFocus()
                            }
                        }

                        else -> Unit
                    }
                    viewModel.processEvent(UserAuthIntent.OTPAuth.ViewEvent.OnAction(action))
                },
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.processEvent(UserAuthIntent.OTPAuth.ViewEvent.VerifyOTP)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = otpState.isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalCustomColors.current.secondaryBackground,
                    contentColor = LocalCustomColors.current.primaryBackground,
                    disabledContainerColor = LocalCustomColors.current.fadedBackground,
                    disabledContentColor = LocalCustomColors.current.defaultImageCardColor
                )
            ) {
                ComposeTextView.TitleTextView(
                    text = stringResource(R.string.verify_otp),
                    textColor = LocalCustomColors.current.primaryBackground,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ComposeTextView.TextView(
                    text = stringResource(R.string.not_get_email),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        viewModel.processEvent(UserAuthIntent.OTPAuth.ViewEvent.ResendOTP)
                    }
                )

                Spacer(Modifier.width(2.dp))

                ComposeTextView.TextView(
                    text = stringResource(R.string.resend_email),
                    textColor = LocalCustomColors.current.secondaryBackground,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        viewModel.processEvent(UserAuthIntent.OTPAuth.ViewEvent.ResendOTP)
                    }
                )
            }
        }
    }
}

@Composable
fun OTPVerificationTextField(
    otpState: OTPState,
    focusRequesters: List<FocusRequester>,
    onAction: (otpAction: OTPAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            otpState.code.forEachIndexed { index, number ->
                OTPInputField(
                    number = number,
                    focusRequester = focusRequesters[index],
                    onFocusChanged = { isFocused ->
                        if(isFocused) {
                            onAction(OTPAction.OnChangeFieldFocused(index))
                        }
                    },
                    onNumberChanged = { newNumber ->
                        onAction(OTPAction.OnEnterNumber(index, newNumber))
                    },
                    onKeyboardBack = {
                        onAction(OTPAction.OnKeyboardBack)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Composable
fun OTPInputField(
    modifier: Modifier,
    number: Int?,
    onNumberChanged: (number: Int?) -> Unit,
    focusRequester: FocusRequester,
    onKeyboardBack: () -> Unit,
    onFocusChanged: (isFocus: Boolean) -> Unit
)  {
    val text by remember(number) {
        mutableStateOf(
            TextFieldValue(
                text = number?.toString().orEmpty(),
                selection = TextRange(
                    index = if(number != null) 1 else 0
                )
            )
        )
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = if (number != null) LocalCustomColors.current.secondaryBackground else LocalCustomColors.current.hintTextColor,
                RoundedCornerShape(12.dp)
            )
            .background(LocalCustomColors.current.primaryBackground, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newText ->
                val newNumber = newText.text
                if (newNumber.length <= 1 && newNumber.isDigitsOnly()) {
                    onNumberChanged(newNumber.toIntOrNull())
                }
            },
            cursorBrush = SolidColor(LocalCustomColors.current.secondaryBackground),
            singleLine = true,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W600,
                fontSize = 20.sp,
                color = LocalCustomColors.current.titleTextColor
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            modifier = Modifier
                .padding(10.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                    onFocusChanged(it.isFocused)
                }
                .onKeyEvent { event ->
                    val didPressDelete = event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL
                    if (didPressDelete && number == null) {
                        onKeyboardBack()
                    }
                    false
                },
            decorationBox = { innerBox ->
                innerBox()
                if (!isFocused && number == null) {
                    ComposeTextView.TitleTextView(
                        text = "-",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                    )
                }
            }
        )
    }
}
