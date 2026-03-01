package com.example.bbltripplanner.screens.userTrip.composables

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.entity.User
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun InviteBottomSheet(
    userList: List<User>,
    addUser: (user: User) -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }
    var mainList by remember(name, userList) {
        mutableStateOf(
            userList.filter { it.name.contains(name, ignoreCase = true) }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 16.dp, 16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                ComposeTextView.TitleTextView(
                    stringResource(R.string.search_by_name),
                    fontSize = 16.sp
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(mainList) { index, user ->
                val shape = when (index) {
                    0 -> RoundedCornerShape(12.dp, 12.dp, 2.dp, 2.dp)
                    userList.size - 1 -> RoundedCornerShape(2.dp, 2.dp, 12.dp, 12.dp)
                    else -> RoundedCornerShape(2.dp, 2.dp, 2.dp, 2.dp)
                }

                Box(
                    modifier = Modifier
                        .background(LocalCustomColors.current.defaultImageCardColor, shape)
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
