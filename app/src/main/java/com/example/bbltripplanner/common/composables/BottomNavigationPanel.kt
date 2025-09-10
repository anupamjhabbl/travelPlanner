package com.example.bbltripplanner.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.entity.BottomNavigationItem
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun BottomNavigationPanel(
    navController: NavHostController,
    selectedTabIndex: Int,
    navigationItemList: List<BottomNavigationItem>
) {
    NavigationBar(
        modifier = Modifier
            .background(LocalCustomColors.current.primaryBackground)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))

    ) {
        navigationItemList.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    navController.navigate(navigationItem.route)
                },
                icon = {
                    NavigationItemIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = navigationItem.selectedIcon,
                        unselectedIcon = navigationItem.unselectedIcon,
                        title = navigationItem.title,
                        badgeAmount = navigationItem.badgeAmount
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                label = { ComposeTextView.TextView(navigationItem.title) }
            )
        }
    }
}

@Composable
fun NavigationItemIconView(
    isSelected: Boolean,
    selectedIcon: Int,
    unselectedIcon: Int,
    title: String,
    badgeAmount: Int?
) {
    val navigationItemSize = if (title == Constants.BottomNavigationItem.ADD) 48.dp else 36.dp
    BadgedBox(
        modifier = Modifier.wrapContentSize(),
        badge = { BottomNavigationItemBadgeView(badgeAmount) }
    ) {
        Icon(
            modifier = Modifier.size(navigationItemSize),
            painter = if (isSelected) painterResource(selectedIcon) else painterResource(unselectedIcon),
            contentDescription = title,
            tint = Color.Unspecified
        )
    }
}

@Composable
fun BottomNavigationItemBadgeView(badgeAmount: Int?) {
    if (badgeAmount != null) {
        Badge {
            Text(badgeAmount.toString())
        }
    }
}
