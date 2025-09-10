package com.example.bbltripplanner.screens.home.composables.widgets

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.utils.WindowSizeUtils
import com.example.bbltripplanner.common.utils.openDeeplink
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget
import com.example.bbltripplanner.screens.home.entities.TravelThreadsWidgetItem

@Composable
fun HomeTravelThreadsBundleComposable(widget: HomeCxeWidget.TravelThreadsBundleWidget) {
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
                TravelThreadWidgetItem(item, index, widgetItemList.size)
            }

            item {
                ViewMoreCTA(widget.data.content?.deeplink)
            }
        }
    }
}

@Composable
fun ViewMoreCTA(deeplink: String?) {
    val context = LocalContext.current
    ComposeImageView.ImageViewWitDrawableId(
        imageId = R.drawable.ic_chevron_right,
        contentDescription = "View All",
        modifier = Modifier
            .size(50.dp)
            .clickable {
                context.openDeeplink(deeplink)
            }
    )
}

@Composable
fun TravelThreadWidgetItem(
    travelThreadsWidgetItem: TravelThreadsWidgetItem,
    index: Int,
    size: Int
) {
    val context = LocalContext.current
    val starPadding =  if (index == 0) 16.dp else 8.dp
    val endPadding = if (index == size - 1) 16.dp else 8.dp
    val width = ((WindowSizeUtils.getWindowWidth(LocalContext.current).toFloat() /  LocalContext.current.resources.displayMetrics.density) - 32) * 0.75f
    val imageList = travelThreadsWidgetItem.tripImages
    var currentIndex by remember {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier
            .width(Dp(width))
            .padding(starPadding, 0.dp, endPadding, 0.dp)
            .border(1.dp, colorResource(R.color.bg_default_image), RoundedCornerShape(12.dp))
            .clickable {
                openTravelThread(context, travelThreadsWidgetItem.threadId)
            }
    ) {
        Box {
            ComposeImageView.ImageViewWithUrl(
                imageURI = imageList[currentIndex],
                contentDescription = "Post Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dp(width / 2))
                    .clip(RoundedCornerShape(12.dp))
            )

            ComposeImageView.ImageViewWitDrawableId(
                imageId = R.drawable.ic_chevron_right,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        currentIndex = (currentIndex + 1) % imageList.size
                    }
            )
        }

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            ComposeTextView.TitleTextView(
                modifier = Modifier.fillMaxWidth(),
                text = travelThreadsWidgetItem.tripName,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))


            ComposeTextView.TextView(
                modifier = Modifier.fillMaxWidth(),
                text = travelThreadsWidgetItem.experienceDescription,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    ComposeImageView.CircularImageView(
                        imageURI = travelThreadsWidgetItem.tripProfile?.profileImage ?: "",
                        contentDescription = "Profile picture",
                        diameter = 20.dp
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    ComposeTextView.TextView(
                        text = travelThreadsWidgetItem.tripProfile?.profileName ?: ""
                    )
                }

                ComposeTextView.TextView(
                    text = stringResource(
                        R.string.views_count,
                        travelThreadsWidgetItem.postEngagement.views
                    )
                )
            }
        }
    }

}

private fun openTravelThread(context: Context, threadId: String) {}
