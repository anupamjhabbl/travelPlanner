package com.example.bbltripplanner.screens.userTrip.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.CommonLifecycleAwareLaunchedEffect
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.navigation.AppNavigationScreen
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
fun TripGroupScreen(
    tripId: String,
    isAdmin: Boolean
) {
    val viewModel: TripGroupViewModel = koinViewModel(parameters = { parametersOf(tripId) })
    val viewState by viewModel.viewState.collectAsState()
    val context = LocalContext.current
    val successMessage = stringResource(R.string.member_added_success)
    val sheetState = rememberModalBottomSheetState()
    var showInviteSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    CommonLifecycleAwareLaunchedEffect(viewModel.viewEffect) { effect ->
        when (effect) {
            is TripGroupIntent.ViewEffect.ShowError -> {
                val message = getMessage(context, effect.message) ?: effect.message
                ComposeViewUtils.showToast(context, message)
            }
            TripGroupIntent.ViewEffect.ShowSuccess -> {
                ComposeViewUtils.showToast(context, successMessage)
            }
        }
    }

    val acceptedMembers = viewState.tripMembers.filter { it.status == TripMemberStatus.ACCEPTED }
    val pendingMembers = viewState.tripMembers.filter { it.status == TripMemberStatus.PENDING }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.primaryBackground)
    ) {
        TripGroupToolbar(memberCount = viewState.tripMembers.size)

        Box(modifier = Modifier.weight(1f)) {
            if (viewState.isLoading) {
                ComposeViewUtils.FullScreenLoading()
            } else if (viewState.error != null) {
                val errorStrings = when (viewState.error) {
                    Constants.ErrorType.NETWORK_ERROR -> Pair(stringResource(R.string.no_internet_connection), stringResource(R.string.no_internet_connection_subtitle))
                    Constants.ErrorType.SERVER_ERROR -> Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
                    Constants.ErrorType.NOT_FOUND -> Pair(stringResource(R.string.nothing_to_show), stringResource(R.string.noting_to_show_subtitle))
                    Constants.ErrorType.NOT_AUTHORIZED -> Pair(stringResource(R.string.not_authorized_title), stringResource(R.string.not_authorized_subtitle))
                    else -> Pair(stringResource(R.string.server_error), viewState.error ?: stringResource(R.string.server_error_subtitle))
                }
                ComposeViewUtils.FullScreenErrorComposable(
                    errorStrings = errorStrings,
                    isActionButton = true,
                    onActionButtonClick = {
                        viewModel.processEvent(TripGroupIntent.ViewEvent.GetTripMembers)
                    }
                )
            } else if (viewState.tripMembers.isEmpty()) {
                TripGroupEmptyState(
                    onInviteClick = {
                        viewModel.processEvent(TripGroupIntent.ViewEvent.GetInviteList)
                        showInviteSheet = true
                    }
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        CrewOverviewCard(
                            acceptedCount = acceptedMembers.size,
                            pendingCount = pendingMembers.size
                        )
                    }

                    if (acceptedMembers.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            SectionHeader(title = stringResource(R.string.active_crew), count = acceptedMembers.size)
                        }
                        items(acceptedMembers) { member ->
                            MemberItem(
                                member = member,
                                onMemberClick = { userId ->
                                    scope.launch {
                                        CommonNavigationChannel.navigateTo(
                                            NavigationAction.Navigate(
                                                AppNavigationScreen.ProfileScreen.createRoute(userId)
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }

                    if (pendingMembers.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            SectionHeader(title = stringResource(R.string.pending_invitataion), count = pendingMembers.size)
                        }
                        items(pendingMembers) { member ->
                            MemberItem(
                                member = member,
                                onMemberClick = { userId ->
                                    scope.launch {
                                        CommonNavigationChannel.navigateTo(
                                            NavigationAction.Navigate(
                                                AppNavigationScreen.ProfileScreen.createRoute(userId)
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(88.dp))
                    }
                }

                if (isAdmin) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(30.dp))
                                .background(LocalCustomColors.current.secondaryBackground)
                                .clickable {
                                    viewModel.processEvent(TripGroupIntent.ViewEvent.GetInviteList)
                                    showInviteSheet = true
                                }
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Invite Tripmates",
                                tint = LocalCustomColors.current.primaryBackground,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            ComposeTextView.TextView(
                                text = stringResource(R.string.invite_tripmates),
                                textColor = LocalCustomColors.current.primaryBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
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
                isError = viewState.isFollowersError,
                errorMessage = getMessage(context, viewState.followersErrorMessage),
                addUser = { user ->
                    viewModel.processEvent(TripGroupIntent.ViewEvent.AddMember(user))
                    showInviteSheet = false
                }
            )
        }
    }
}

@Composable
fun TripGroupToolbar(memberCount: Int) {
    val scope = rememberCoroutineScope()
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(customColors.secondaryBackground.copy(alpha = 0.12f))
                .clickable {
                    scope.launch {
                        CommonNavigationChannel.navigateTo(NavigationAction.NavigateUp)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = customColors.secondaryBackground,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            ComposeTextView.TitleTextView(
                text = stringResource(R.string.trip_members),
                fontSize = 20.sp,
                textColor = customColors.titleTextColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            ComposeTextView.TextView(
                text = stringResource(R.string.trip_members_total, memberCount),
                fontSize = 12.sp,
                textColor = customColors.hintTextColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CrewOverviewCard(acceptedCount: Int, pendingCount: Int) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(customColors.fadedBackground.copy(alpha = 0.35f))
            .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(customColors.secondaryBackground.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.People,
                contentDescription = null,
                tint = customColors.secondaryBackground,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            ComposeTextView.TextView(
                text = stringResource(R.string.crew_summary),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textColor = customColors.titleTextColor
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF10B981), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                ComposeTextView.TextView(
                    text = stringResource(R.string.active_crew_count, acceptedCount),
                    fontSize = 13.sp,
                    textColor = customColors.textColor,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFFF59E0B), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                ComposeTextView.TextView(
                    text = stringResource(R.string.pending_crew_count, pendingCount),
                    fontSize = 13.sp,
                    textColor = customColors.textColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, count: Int) {
    val customColors = LocalCustomColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ComposeTextView.TextView(
            text = title.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            textColor = customColors.hintTextColor
        )

        Box(
            modifier = Modifier
                .background(customColors.secondaryBackground.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            ComposeTextView.TextView(
                text = count.toString(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textColor = customColors.secondaryBackground
            )
        }
    }
}

@Composable
fun MemberItem(member: TripMember, onMemberClick: (String) -> Unit) {
    val customColors = LocalCustomColors.current
    val statusColor = when (member.status) {
        TripMemberStatus.ACCEPTED -> Color(0xFF10B981)
        TripMemberStatus.PENDING -> Color(0xFFF59E0B)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(customColors.defaultImageCardColor.copy(alpha = 0.35f))
            .border(1.dp, customColors.defaultImageCardColor, RoundedCornerShape(20.dp))
            .clickable { onMemberClick(member.user.id) }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(2.dp, statusColor.copy(alpha = 0.4f), CircleShape)
                .padding(2.dp)
        ) {
            ComposeImageView.CircularImageView(
                imageURI = member.user.profilePicture ?: "",
                diameter = 44.dp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            ComposeTextView.TextView(
                text = member.user.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textColor = customColors.titleTextColor
            )

            if (!member.user.bio.isNullOrEmpty()) {
                Spacer(Modifier.height(4.dp))
                ComposeTextView.TextView(
                    text = member.user.bio,
                    fontSize = 12.sp,
                    textColor = customColors.hintTextColor,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        StatusBadge(member.status)
    }
}

@Composable
fun StatusBadge(status: TripMemberStatus) {
    val (color, icon, text) = when (status) {
        TripMemberStatus.ACCEPTED -> Triple(Color(0xFF10B981), Icons.Default.CheckCircle, stringResource(R.string.status_joined))
        TripMemberStatus.PENDING -> Triple(Color(0xFFF59E0B), Icons.Default.Pending, stringResource(R.string.status_pending))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        ComposeTextView.TextView(
            text = text,
            fontSize = 11.sp,
            textColor = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TripGroupEmptyState(onInviteClick: () -> Unit) {
    val customColors = LocalCustomColors.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(customColors.secondaryBackground.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.People,
                contentDescription = null,
                tint = customColors.secondaryBackground,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ComposeTextView.TitleTextView(
            text = stringResource(R.string.empty_state_title),
            fontSize = 20.sp,
            textColor = customColors.titleTextColor,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        ComposeTextView.TextView(
            text = stringResource(R.string.empty_state_desc),
            fontSize = 14.sp,
            textColor = customColors.hintTextColor,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(customColors.secondaryBackground)
                .clickable { onInviteClick() }
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = customColors.primaryBackground,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            ComposeTextView.TextView(
                text = stringResource(R.string.invite_tripmates),
                textColor = customColors.primaryBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}
