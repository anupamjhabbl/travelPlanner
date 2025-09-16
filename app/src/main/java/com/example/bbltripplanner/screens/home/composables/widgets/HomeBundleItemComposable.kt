package com.example.bbltripplanner.screens.home.composables.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.utils.WindowSizeUtils
import com.example.bbltripplanner.navigation.AppNavigationScreen
import com.example.bbltripplanner.screens.home.entities.BundleWidgetItem
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget
import com.example.bbltripplanner.ui.theme.LocalCustomColors

@Composable
fun HomeBundleItemComposable(
    navController: NavController,
    widget: HomeCxeWidget.BundleItemsWidget
) {
    val widgetItemList = widget.data.widgetList

    if (widgetItemList.isNullOrEmpty()) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ComposeTextView.TitleTextView(
            modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp),
            text = widget.data.header?.text ?: "",
            fontSize = 18.sp,
        )

        Spacer(Modifier.height(8.dp))

        LazyRow(
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(widgetItemList) { index, item ->
                BundleItemView(item, index, widgetItemList.size) { itemId ->
                    navController.navigate(AppNavigationScreen.DestinationScreen.createRoute(itemId))
                }
            }

            item {
                ViewMoreCTA(widget.data.content?.deeplink)
            }
        }
    }
}

@Composable
fun BundleItemView(
    bundleWidgetItem: BundleWidgetItem,
    index: Int,
    size: Int,
    onClick: (String) -> Unit
) {
    val width = ((WindowSizeUtils.getWindowWidth(LocalContext.current).toFloat() /  LocalContext.current.resources.displayMetrics.density) - 32) * 0.75f
    val starPadding =  if (index == 0) 16.dp else 8.dp
    val endPadding = if (index == size - 1) 16.dp else 8.dp
    val imageList = bundleWidgetItem.tripImage
    var currentIndex by remember {
        mutableIntStateOf(0)
    }

    Box(
        modifier = Modifier
            .width(Dp(width))
            .height(Dp(width/2))
            .padding(starPadding, 0.dp, endPadding, 0.dp)
            .clickable {
                onClick(bundleWidgetItem.itemId)
            }
    ) {
        Box {
            ComposeImageView.ImageViewWithUrl(
                imageURI = imageList[currentIndex],
                contentDescription = "Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            ComposeImageView.ImageViewWitDrawableId(
                imageId = R.drawable.ic_chevron_right,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterEnd)
                    .clickable(interactionSource = null, indication = null) {
                        currentIndex = (currentIndex + 1) % imageList.size
                    }
            )
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = LocalCustomColors.current.defaultImageCardColor, RoundedCornerShape(8.dp))
                .padding(16.dp, 8.dp)
                .wrapContentSize()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ComposeTextView.TitleTextView(
                text = bundleWidgetItem.tripName,
                textColor = LocalCustomColors.current.secondaryBackground,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
    }
}
