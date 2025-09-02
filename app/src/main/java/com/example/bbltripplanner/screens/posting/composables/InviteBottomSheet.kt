package com.example.bbltripplanner.screens.posting.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.entity.Contact
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.common.entity.UserSettings

@Composable
fun InviteBottomSheet(
    addUser: (user: User) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 16.dp, 16.dp)
    ) {
        val userList = listOf(
            User(
                id = 1,
                name = "Robert Hunt",
                contact = Contact(listOf("angelarasmussen@herrera.com"), "001-396-200-8778"),
                bio = "Service your movie up eye light vote.",
                followCount = 672,
                followersCount = 99,
                tripCount = 42,
                createdAt = "2022-08-28T08:08:58",
                updatedAt = "2022-11-06T10:53:13",
                userSettings = UserSettings(Constants.Theme.DARK),
                profilePicture = "https://placeimg.com/967/1007/any"
            ),
            User(
                id = 2,
                name = "Cole Adams",
                contact = Contact(listOf("haley17@yahoo.com"), "+1-788-055-3737"),
                bio = "Mission deal yes inside agent around.",
                followCount = 212,
                followersCount = 3682,
                tripCount = 9,
                createdAt = "2024-11-25T11:03:37",
                updatedAt = "2020-09-07T01:51:57",
                userSettings = UserSettings(Constants.Theme.DARK),
                profilePicture = "https://placeimg.com/729/117/any"
            ),
            User(
                id = 3,
                name = "Amanda Phillips",
                contact = Contact(listOf("zsims@gmail.com"), "569.628.5394x204"),
                bio = "Same during interview inside course fly despite.",
                followCount = 752,
                followersCount = 701,
                tripCount = 44,
                createdAt = "2023-10-28T10:24:07",
                updatedAt = "2021-05-16T12:06:23",
                userSettings = UserSettings(Constants.Theme.DARK),
                profilePicture = "https://dummyimage.com/67x468"
            ),
            User(
                id = 4,
                name = "Katherine Shaffer",
                contact = Contact(listOf("brian26@gmail.com"), "(668)666-0287x4544"),
                bio = "Whether interest but education.",
                followCount = 598,
                followersCount = 671,
                tripCount = 12,
                createdAt = "2022-07-13T18:38:32",
                updatedAt = "2024-08-28T12:35:52",
                userSettings = UserSettings(Constants.Theme.DARK),
                profilePicture = "https://dummyimage.com/631x600"
            )
        )
        OutlinedTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                ComposeTextView.TitleTextView(
                    "Search by name, email, contact",
                    fontSize = 16.sp
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(userList) { index, user ->
                val shape = when (index) {
                    0 -> RoundedCornerShape(12.dp, 12.dp, 2.dp, 2.dp)
                    userList.size - 1 -> RoundedCornerShape(2.dp, 2.dp, 12.dp, 12.dp)
                    else -> RoundedCornerShape(2.dp, 2.dp, 2.dp, 2.dp)
                }

                Box(
                    modifier = Modifier
                        .background(colorResource(R.color.bg_default_image), shape)
                        .height(50.dp)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable {
                            addUser(user)
                        }
                ) {
                    ComposeTextView.TitleTextView(
                        text = user.name,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

                Spacer(Modifier.height(1.dp))
            }
        }
    }
}
