package com.example.bbltripplanner.screens.home.composables.widgets

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.utils.WindowSizeUtils
import com.example.bbltripplanner.screens.home.entities.CarouselItemSrc
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeImageCarouselComposable(widget: HomeCxeWidget.ImageCarouselWidget) {
    val imageCarouselItems = widget.data.carouselItems ?: return
    val pagerState = rememberPagerState()
    val context = LocalContext.current
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    LaunchedEffect(pagerState.currentPage) {
        delay(3000)
        pagerState.animateScrollToPage((pagerState.currentPage + 1) % imageCarouselItems.size)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            count = imageCarouselItems.size
        ) {  pageNo ->
            Card(
                modifier = Modifier.padding(16.dp, 0.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                imageCarouselItems[pageNo].src?.let { carouselItem ->
                    ImageCarouselItem(carouselItem, widget.metadata?.style?.aspectRatio) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(carouselItem.action))
                        context.startActivity(intent)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DotsIndicator(
                totalDots = imageCarouselItems.size,
                selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage
            )
        }
    }
}

@Composable
fun ImageCarouselItem(itemSrc: CarouselItemSrc, aspectRatio: String?, onClick: (actionDeeplink: String) -> Unit) {
    val width = (WindowSizeUtils.getWindowWidth(LocalContext.current).toFloat() /  LocalContext.current.resources.displayMetrics.density) - 64
    val ratioPair = getRatio(aspectRatio)
    val height = (width * ratioPair.second) / ratioPair.first
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dp(height))
            .clip(RoundedCornerShape(16.dp))
    ) {
        ComposeImageView.ImageViewWithUrl(
            imageURI = itemSrc.uri ?: "",
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                itemSrc.action?.let(onClick)
            }
        )
    }
}

fun getRatio(aspectRatio: String?): Pair<Int, Int> {
    if (aspectRatio == null) {
        return Pair(2, 1)
    }
    val ratios = aspectRatio.split(":")
    return if (ratios.size == 2) {
        Pair(ratios[0].toInt(), ratios[1].toInt())
    }  else {
        Pair(2, 1)
    }
}

@Composable
fun IndicatorDot(
    color: Color
) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int
) {
    val selectedColor = colorResource(R.color.primary)
    val unSelectedColor = colorResource(R.color.faded_primary)
    LazyRow(
        modifier = modifier.wrapContentSize()
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            }
        }
    }
}
