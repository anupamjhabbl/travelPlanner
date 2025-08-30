package com.example.bbltripplanner.screens.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bbltripplanner.R
import com.example.bbltripplanner.common.Constants
import com.example.bbltripplanner.common.composables.ComposeImageView
import com.example.bbltripplanner.common.composables.ComposeTextView
import com.example.bbltripplanner.common.composables.ComposeViewUtils
import com.example.bbltripplanner.screens.home.composables.widgets.HomeBundleItemComposable
import com.example.bbltripplanner.screens.home.composables.widgets.HomeGreetingComposable
import com.example.bbltripplanner.screens.home.composables.widgets.HomeImageCarouselComposable
import com.example.bbltripplanner.screens.home.composables.widgets.HomeLocationFeedCtaComposable
import com.example.bbltripplanner.screens.home.composables.widgets.HomeNewsBannerComposable
import com.example.bbltripplanner.screens.home.composables.widgets.HomeTravelThreadsBundleComposable
import com.example.bbltripplanner.screens.home.composables.widgets.HomeUserTripBundleWidgetComposable
import com.example.bbltripplanner.screens.home.entities.CxeResponseError
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget
import com.example.bbltripplanner.screens.home.viewModels.HomeExperienceIntent
import com.example.bbltripplanner.screens.home.viewModels.HomeExperienceViewModel
import com.example.bbltripplanner.ui.theme.LocalCustomColors
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeExperienceScreen(
    navController: NavController
) {
    val viewModel: HomeExperienceViewModel = koinViewModel()
    val widgets by viewModel.widgetsLiveData.collectAsState()
    val viewState by viewModel.viewStateLiveData.collectAsState()
    val widgetsListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        launch {
            viewModel.viewEffectLiveData.collect { viewEffect ->
                when (viewEffect) {
                    HomeExperienceIntent.ViewEffect.GoToLocationFeedScreen -> {}
                }
            }
        }
        viewModel.processEvent(HomeExperienceIntent.ViewEvent.Initialize)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.primaryBackground)
    ) {
        when (val state = viewState) {
            HomeExperienceIntent.ViewState.ShowFullScreenLoading -> FullScreenLoading()
            is HomeExperienceIntent.ViewState.ShowCxeResponseError -> ShowCxeResponseErrorComposable(
                state.error
            )
            else -> {
                val uiWidgetList = filterWidgetsForUI(widgets)
                ShowItems(uiWidgetList, widgetsListState)
            }
        }
    }
}

fun filterWidgetsForUI(widgets: List<HomeCxeWidget>): List<HomeCxeWidget> {
    return widgets.filter {
        when (it) {
            is HomeCxeWidget.TopPicksByLocationCtaWidget -> true
            is HomeCxeWidget.BundleItemsWidget -> it.data.widgetList?.isEmpty() != true
            is HomeCxeWidget.GreetingWidget, is HomeCxeWidget.ImageCarouselWidget, is HomeCxeWidget.NewsBannerWidget -> true
            is HomeCxeWidget.TravelThreadsBundleWidget -> it.data.widgetList?.isEmpty() != true
            is HomeCxeWidget.UserTripBundleWidget -> it.data.widgetList?.isEmpty() != true
        }
    }
}

@Composable
fun ShowItems(widgets: List<HomeCxeWidget>, widgetsListState: LazyListState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = widgetsListState
    ) {
        item { HomeToolbar() }

        items(widgets) { homeCxeWidget ->
            Spacer(modifier = Modifier.height(14.dp))

            when (homeCxeWidget) {
                is HomeCxeWidget.GreetingWidget -> HomeGreetingComposable(homeCxeWidget, "Jeevesh")
                is HomeCxeWidget.ImageCarouselWidget -> HomeImageCarouselComposable(homeCxeWidget)
                is HomeCxeWidget.BundleItemsWidget -> HomeBundleItemComposable(homeCxeWidget)
                is HomeCxeWidget.NewsBannerWidget -> HomeNewsBannerComposable(homeCxeWidget)
                is HomeCxeWidget.TopPicksByLocationCtaWidget -> HomeLocationFeedCtaComposable(homeCxeWidget)
                is HomeCxeWidget.TravelThreadsBundleWidget -> HomeTravelThreadsBundleComposable(homeCxeWidget)
                is HomeCxeWidget.UserTripBundleWidget -> HomeUserTripBundleWidgetComposable(homeCxeWidget)
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        item {
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun HomeToolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.white))
            .padding(16.dp, 0.dp, 16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ComposeImageView.ImageViewWitDrawableId(
                imageId = R.drawable.ic_home_filled,
                modifier = Modifier.size(30.dp),
                contentDescription = "search"
            )

            Spacer(Modifier.width(8.dp))

            ComposeTextView.TitleTextView(
                text = Constants.APP_NAME
            )
        }

        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(colorResource(R.color.primary), CircleShape)
                    .padding(6.dp)
            ) {
                ComposeImageView.ImageViewWitDrawableId(
                    imageId = R.drawable.ic_search,
                    modifier = Modifier.size(18.dp),
                    contentDescription = "search"
                )
            }

            Spacer(Modifier.width(12.dp))

            ComposeImageView.ImageViewWitDrawableId(
                imageId = R.drawable.ic_notification_action,
                modifier = Modifier.size(30.dp),
                contentDescription = "search"
            )
        }
    }
}

@Composable
fun ShowCxeResponseErrorComposable(error: CxeResponseError) {
    Text("Error")
}

@Composable
fun FullScreenLoading() {
    ComposeViewUtils.Loading(
        modifier = Modifier.size(40.dp)
    )
}