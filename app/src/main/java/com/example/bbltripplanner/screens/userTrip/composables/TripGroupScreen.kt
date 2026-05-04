package com.example.bbltripplanner.screens.userTrip.composables

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.CommonLifecycleAwareLaunchedEffect
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.navigation.CommonNavigationChannel
import com.example.bbltripplanner.navigation.NavigationAction
import com.example.bbltripplanner.screens.userTrip.entity.TripMember
import com.example.bbltripplanner.screens.userTrip.entity.TripMemberStatus
import com.example.bbltripplanner.screens.userTrip.viewModels.TripGroupIntent
import com.example.bbltripplanner.screens.userTrip.viewModels.TripGroupViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripGroupScreen(tripId: String) {
    val viewModel: TripGroupViewModel = koinViewModel(parameters = { parametersOf(tripId) })
    val viewState by viewModel.viewState.collectAsState()
    val context = LocalContext.current
    val successMessage = stringResource(R.string.member_added_success)
    val sheetState = rememberModalBottomSheetState()
    var showInviteSheet by remember { mutableStateOf(false) }

    CommonLifecycleAwareLaunchedEffect(viewModel.viewEffect) { effect ->
        when (effect) {
            is TripGroupIntent.ViewEffect.ShowError -> {
                ComposeViewUtils.showToast(context, effect.message)
            }
            TripGroupIntent.ViewEffect.ShowSuccess -> {
                ComposeViewUtils.showToast(context, successMessage)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.primaryBackground)
    ) {
        TripGroupToolbar()

        Box(modifier = Modifier.weight(1f)) {
            if (viewState.isLoading) {
                ComposeViewUtils.FullScreenLoading()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewState.tripMembers) { member ->
                        MemberItem(member)
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                IconButton(
                    onClick = {
                        viewModel.processEvent(TripGroupIntent.ViewEvent.GetInviteList)
                        showInviteSheet = true
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .background(LocalCustomColors.current.secondaryBackground, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Member",
                        tint = LocalCustomColors.current.primaryBackground
                    )
                }
            }
        }
    }

    if (showInviteSheet) {
        ModalBottomSheet(
            onDismissRequest = { showInviteSheet = false },
            sheetState = sheetState,
            containerColor = LocalCustomColors.current.primaryBackground
        ) {
            InviteBottomSheet(
                userList = viewState.inviteList,
                isFollowersLoading = viewState.isFollowersLoading,
                addUser = { user ->
                    viewModel.processEvent(TripGroupIntent.ViewEvent.AddMember(user))
                    showInviteSheet = false
                }
            )
        }
    }
}

@Composable
fun MemberItem(member: TripMember) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LocalCustomColors.current.defaultImageCardColor)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ComposeImageView.CircularImageView(
            imageURI = member.user.profilePicture ?: "",
            diameter = 48.dp
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            ComposeTextView.TextView(
                text = member.user.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            ComposeTextView.TextView(
                text = member.user.email ?: "",
                fontSize = 12.sp,
                textColor = LocalCustomColors.current.fadedBackground
            )
        }

        StatusBadge(member.status)
    }
}

@Composable
fun StatusBadge(status: TripMemberStatus) {
    val (color, icon, text) = when (status) {
        TripMemberStatus.ACCEPTED -> Triple(Color(0xFF4CAF50), Icons.Default.CheckCircle, "Accepted")
        TripMemberStatus.PENDING -> Triple(Color(0xFFFF9800), Icons.Default.Pending, "Pending")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        ComposeTextView.TextView(
            text = text,
            fontSize = 12.sp,
            textColor = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TripGroupToolbar() {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                    }
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LocalCustomColors.current.secondaryBackground
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        ComposeTextView.TitleTextView(
            text = stringResource(R.string.trip_members),
            fontSize = 20.sp
        )
    }
}
