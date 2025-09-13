package com.example.bbltripplanner.screens.user.profile.composables

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.common.composables.ComposeViewUtils.Menu
import com.example.bbltripplanner.common.entity.MenuItems
import com.example.bbltripplanner.common.entity.RequestStatus
import com.example.bbltripplanner.common.utils.StringUtils
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.screens.posting.composables.showToast
import com.example.bbltripplanner.screens.user.myacount.entity.ProfileActionItem
import com.example.bbltripplanner.screens.user.profile.entity.ProfileSocialScreens
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileIntent
import com.example.bbltripplanner.screens.user.profile.viewModels.ProfileViewModel
import com.example.bbltripplanner.screens.vault.entity.VaultScreens
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen (
    navController: NavController,
    userId: String?
) {
    val profileViewModel: ProfileViewModel = koinViewModel()
    val userData by profileViewModel.userData.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val blockFailureMessage = stringResource(R.string.block_failure)
    val blockSuccessMessage = stringResource(R.string.block_success)
    val followSuccessMessage = stringResource(R.string.follow_success)
    val followFailureMessage = stringResource(R.string.follow_failure)

    LaunchedEffect(Unit) {
        userId?.let {
            profileViewModel.processEvent(ProfileIntent.ViewEvent.SetUp(userId))
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.viewState.collectLatest { viewState ->
            when (viewState){
                ProfileIntent.ViewState.BlockFailure -> showToast(context, blockFailureMessage)
                ProfileIntent.ViewState.BlockSuccess -> showToast(context, blockSuccessMessage)
                ProfileIntent.ViewState.FollowFailure -> showToast(context, followFailureMessage)
                ProfileIntent.ViewState.FollowSuccess -> showToast(context, followSuccessMessage)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(LocalCustomColors.current.primaryBackground, LocalCustomColors.current.deepPurpleGlow)
                )
            )
            .padding(16.dp, 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (userData) {
                RequestStatus.Idle -> {}

                RequestStatus.Loading -> ComposeViewUtils.FullScreenLoading()

                is RequestStatus.Success -> {
                    val user = userData as RequestStatus.Success
                    val profileActionList = profileViewModel.getProfileActionList()
                    val profileMenuItems = profileViewModel.getProfileMenuItem()

                    ProfileViewToolbar(profileViewModel,  navController, profileMenuItems, user.data.id)

                    ProfileTpCommonSectionComposable(
                        user.data,
                        navController,
                        profileViewModel.isMyProfile()
                    ) {
                        profileViewModel.processEvent(ProfileIntent.ViewEvent.FollowUser)
                    }

                    Spacer(Modifier.height(38.dp))

                    ProfileActionComposable(
                        profileActionList,
                        navController,
                        user.data.id
                    )
                }

                is RequestStatus.Error -> {
                    ComposeViewUtils.FullScreenErrorComposable(
                        Pair(
                            stringResource(R.string.inconvenience_sorry),
                            stringResource(R.string.restart_app_message)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileActionComposable(
    profileActionList: List<ProfileActionItem>,
    navController: NavController,
    userId: String
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(
            profileActionList,
            key = { _, item ->
                item.key
            }
        ) { index, item ->
            val shape = when (index) {
                0 -> RoundedCornerShape(12.dp, 12.dp, 4.dp, 4.dp)
                profileActionList.size - 1 -> RoundedCornerShape(
                    4.dp,
                    4.dp,
                    12.dp,
                    12.dp
                )
                else -> RoundedCornerShape(2.dp, 2.dp, 2.dp, 2.dp)
            }

            Box(
                modifier = Modifier
                    .background(
                        LocalCustomColors.current.defaultImageCardColor,
                        shape
                    )
                    .clickable {
                        takeAction(navController, item.key, userId)
                    }
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ) {
                ProfileActionItem(stringResource(item.title), item.vectorId)
            }

            Spacer(Modifier.height(1.dp))
        }
    }
}

@Composable
fun ProfileViewToolbar(
    profileViewModel: ProfileViewModel,
    navController: NavController,
    profileMenuItems: List<String>,
    userId: String
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color = LocalCustomColors.current.secondaryBackground, CircleShape)
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LocalCustomColors.current.primaryBackground
                )
            }
        }

        Menu(
            menuItems = profileMenuItems,
            { item ->
                doMenuAction(context, profileViewModel, navController, item, userId)
            },
        )
    }
}

private fun doMenuAction(
    context: Context,
    profileViewModel: ProfileViewModel,
    navController: NavController,
    item: String,
    userId: String
) {
    when (item) {
        MenuItems.MyProfileMenuItem.EDIT.value -> {
            navController.navigate(AppNavigationScreen.EditProfileScreen.route)
        }
        MenuItems.MyProfileMenuItem.SHARE.value -> shareProfile(context, userId)
        MenuItems.OtherProfileMenuItem.BLOCK.value -> {
            profileViewModel.processEvent(ProfileIntent.ViewEvent.BlockUser)
        }
    }
}

fun shareProfile(
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

private fun takeAction(navController: NavController, key: String, userId: String) {
    when (key) {
        Constants.TRIP_PAGE -> navController.navigate(AppNavigationScreen.VaultScreen.createRoute(
            VaultScreens.TRIPS.value,
            userId
        ))
        Constants.FAVOURITES -> navController.navigate(AppNavigationScreen.VaultScreen.createRoute(
            VaultScreens.FAVOURITES.value,
            userId
        ))
        Constants.REVIEW_PAGES -> navController.navigate(AppNavigationScreen.VaultScreen.createRoute(
            VaultScreens.THREADS.value,
            userId
        ))
        Constants.BUZZ_PAGE -> navController.navigate(AppNavigationScreen.VaultScreen.createRoute(
            VaultScreens.BUZZ.value,
            userId
        ))
        Constants.CONTACTS -> navController.navigate(AppNavigationScreen.ProfileSocialScreen.createRoute(ProfileSocialScreens.CONTACTS.value, userId))
    }
}

@Composable
private fun ProfileActionItem(
    title: String,
    icon: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ComposeImageView.ImageViewWitDrawableId(
            imageId = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(16.dp))

        ComposeTextView.TitleTextView(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = LocalCustomColors.current.secondaryBackground
        )

    }
}

