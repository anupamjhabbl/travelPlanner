package com.example.tripplanner.user.myacount.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripplanner.R
import com.example.tripplanner.common.composables.ComposeImageView
import com.example.tripplanner.common.composables.ComposeTextView
import com.example.tripplanner.common.composables.TitleBarView
import com.example.tripplanner.ui.theme.LocalCustomColors

val profileActionList = listOf(
    ProfileActionItem("profile_details", R.drawable.ic_profile_details_action, "Profile Details", "View and edit your profile"),
    ProfileActionItem("notifications", R.drawable.ic_notification_action, "Notifications", "view notifications "),
    ProfileActionItem("settings", R.drawable.ic_settings_action, "Settings", "Review and Update profile Settings"),
    ProfileActionItem("help_support", R.drawable.ic_helpsupport_action, "Help & Support", "Help centre and legal terms"),
    ProfileActionItem("logout", R.drawable.ic_logout_action, "Logout", "Logout your profile")
)

@Composable
fun MyAccountView() {
    Scaffold (
        topBar = {
            TitleBarView.TransparentTitleBackButtonTitleBar(
                modifier = Modifier,
                title = stringResource(id = R.string.profile),
                backgroundColor = Color.Transparent
            ) {}
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column (
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.module_16))
                    .wrapContentHeight()
            ) {
                ProfileContainer()
                LazyColumn (
                    modifier = Modifier
                        .padding(0.dp, 30.dp, 0.dp, 16.dp)
                ) {
                    items(profileActionList) { item ->
                        ProfileActionTile(
                            vectorId = item.vectorId,
                            title = item.title,
                            subTitle = item.subTitle
                        ) { key ->
                            takeAction(key)
                        }
                    }
                }
            }
        }

    }
}

fun takeAction(key: String) {}


@Composable
fun ProfileContainer() {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.module_8)))
            .background(LocalCustomColors.current.primaryAccent)
            .padding(dimensionResource(id = R.dimen.module_24))
    ) {
        Row (
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .align(Alignment.Center)
        ){
            ComposeImageView.CircularImageView(
                imageURI = "https://delasign.com/delasignBlack.png",
                diameter = dimensionResource(id = R.dimen.module_90)
            )
            ProfileNameAndTravelsContainer(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.module_8), 0.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun ProfileNameAndTravelsContainer(
    modifier: Modifier
) {
    Column (
        modifier = modifier,
        Arrangement.Center
    ) {
        ComposeTextView.TextView(
            text = "Anupam Kumar",
            fontSize = dimensionResource(id = R.dimen.module_20sp).value.sp,
            textColor = LocalCustomColors.current.textColor
        )
        ComposeTextView.TextView(
            text = "8 Travels",
            fontSize = dimensionResource(id = R.dimen.module_18sp).value.sp,
            textColor = LocalCustomColors.current.textColor
        )
    }
}

@Composable
fun ProfileActionTile (
    vectorId: Int,
    title: String,
    subTitle: String,
    onClick: (key: String) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp, 20.dp, 8.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ComposeImageView.ImageViewWitDrawable(
                imageId = vectorId,
                modifier = Modifier.height(36.dp),
                contentDescription = title
            )

            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(16.dp, 0.dp)
            ) {
                ComposeTextView.TextView(
                    text = title,
                    fontSize = dimensionResource(id = R.dimen.module_18sp).value.sp,
                    fontWeight = FontWeight.W400
                )
                ComposeTextView.TextView(
                    text = subTitle,
                    fontSize = dimensionResource(id = R.dimen.module_16sp).value.sp,
                    fontWeight = FontWeight.W400
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            ComposeImageView.ImageViewWitDrawable(
                imageId = R.drawable.ic_right_arrow,
                modifier = Modifier.height(20.dp),
                contentDescription = "right arrow"
            )
        }
    }
}

data class ProfileActionItem (
    val key: String,
    val vectorId: Int,
    val title: String,
    val subTitle: String
)