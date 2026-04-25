package com.example.bbltripplanner.common.composables

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import com.example.bbltripplanner.R
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch

object ComposeViewUtils {
    @Composable
    fun Loading(
        modifier: Modifier = Modifier,
        color: Color = LocalCustomColors.current.fadedBackground,
        strokeWidth: Dp = 3.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = modifier,
                color = color,
                strokeWidth = strokeWidth
            )
        }
    }

    @Composable
    fun FullScreenErrorComposable(
        errorStrings: Pair<String, String>,
        isActionButton: Boolean = false,
        onActionButtonClick: () -> Unit = {},
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ComposeImageView.ImageViewWitDrawableId(
                imageId = R.drawable.ic_vault_filled,
                contentDescription = "Error",
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ComposeTextView.TitleTextView(
                text = errorStrings.first,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            ComposeTextView.TextView(
                text = errorStrings.second,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isActionButton) {
                Button(
                    onClick = onActionButtonClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(LocalCustomColors.current.secondaryBackground)
                ) {
                    ComposeTextView.TitleTextView(
                        text = "Retry",
                        textColor = LocalCustomColors.current.primaryBackground,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    @Composable
    fun PageUnderProgressScreen(
        pageName: String
    ) {
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ComposeImageView.ImageViewWitDrawableId(
                imageId = R.drawable.ic_vault_filled,
                contentDescription = "Under Progress",
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ComposeTextView.TitleTextView(
                text = stringResource(R.string.work_under_progress),
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            ComposeTextView.TextView(
                text = stringResource(R.string.live_soon, pageName),
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(
                            NavigationAction.Navigate(
                                AppNavigationScreen.HomeScreen.route
                            )
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(LocalCustomColors.current.secondaryBackground)
            ) {
                ComposeTextView.TitleTextView(
                    text = stringResource(R.string.back_to_home),
                    textColor = LocalCustomColors.current.primaryBackground,
                    fontSize = 16.sp
                )
            }
        }
    }

    @Composable
    fun FullScreenLoading() {
        Loading(
            modifier = Modifier.size(40.dp)
        )
    }

    @Composable
    fun Menu(
        menuItems: List<String>,
        onItemClick: (String) -> Unit,
        insideBoxIcon: Boolean = false,
        boxSize: Dp = 36.dp,
        iconSize: Dp = 28.dp
    ) {
        var expanded by remember { mutableStateOf(false) }
        val boxColor =  if (insideBoxIcon) LocalCustomColors.current.secondaryBackground else Color.Transparent
        val tintColor = if  (insideBoxIcon) LocalCustomColors.current.primaryBackground else  LocalCustomColors.current.secondaryBackground

        Box {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(boxColor, CircleShape)
            ) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = tintColor
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true)
            ) {
                menuItems.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            ComposeTextView.TextView(
                                text = item,
                                fontSize = 14.sp
                            )
                        },
                        onClick = {
                            expanded = false
                            onItemClick(item)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }

    fun showToast(context: Context, stringResource: String) {
        Toast.makeText(context, stringResource, Toast.LENGTH_SHORT).show()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ExposedDropDownMenu(
        itemList: List<String>,
        selected: String,
        onChange: (String) -> Unit
    ) {
        var expanded by remember {
            mutableStateOf(false)
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            Box(
                modifier = Modifier
                    .menuAnchor()
                    .background(color = LocalCustomColors.current.secondaryBackground, RoundedCornerShape(50))
                    .height(38.dp)
                    .padding(horizontal = 16.dp)
                    .wrapContentWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ComposeTextView.TitleTextView(
                        selected,
                        fontSize = 14.sp,
                        textColor = LocalCustomColors.current.primaryBackground
                    )

                    Spacer(Modifier.width(2.dp))

                    Icon(
                        Icons.Default.ArrowDropDown,
                        modifier = Modifier.size(24.dp),
                        contentDescription = "DropDown",
                        tint = LocalCustomColors.current.primaryBackground
                    )
                }
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
            ) {
                itemList.forEach { item ->
                    DropdownMenuItem(
                        text = { ComposeTextView.TextView(item) },
                        onClick = {
                            onChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun SuccessPopup(
        title: String = stringResource(R.string.success),
        message: String,
        buttonText: String = stringResource(R.string.see_your_trip),
        onConfirm: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    ComposeTextView.TitleTextView(text = title, fontSize = 24.sp)
                }
            },
            text = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    ComposeTextView.TextView(
                        text = message,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(LocalCustomColors.current.secondaryBackground)
                    ) {
                        ComposeTextView.TitleTextView(
                            text = buttonText,
                            textColor = LocalCustomColors.current.primaryBackground,
                            fontSize = 16.sp
                        )
                    }
                }
            },
            containerColor = LocalCustomColors.current.primaryBackground,
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }
}