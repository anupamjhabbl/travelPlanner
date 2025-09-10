package com.example.bbltripplanner.screens.home.composables

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.primaryBackground)
    ) {
        HomeToolbar(navController = navController)

        when (val state = viewState) {
            HomeExperienceIntent.ViewState.ShowFullScreenLoading -> FullScreenLoading()
            is HomeExperienceIntent.ViewState.ShowCxeResponseError -> ShowCxeResponseErrorComposable(
                state.error
            ) {
                viewModel.processEvent(HomeExperienceIntent.ViewEvent.Initialize)
            }
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
fun HomeToolbar(
    navController: NavController
) {
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
                modifier = Modifier.size(30.dp)
                    .clickable {

                    },
                contentDescription = "search"
            )
        }
    }
}

@Composable
fun ShowCxeResponseErrorComposable(
    error: CxeResponseError,
    onRetryClick: () -> Unit
) {
    val errorStrings = when (error) {
        CxeResponseError.NO_DATA_ERROR -> Pair(stringResource(R.string.nothing_to_show), stringResource(R.string.noting_to_show_subtitle))
        CxeResponseError.INTERNET_ERROR -> Pair(stringResource(R.string.no_internet_connection), stringResource(R.string.no_internet_connection_subtitle))
        CxeResponseError.SERVER_ERROR -> Pair(stringResource(R.string.server_error), stringResource(R.string.server_error_subtitle))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ComposeImageView.ImageViewWitDrawableId(
            imageId = R.drawable.ic_vault_filled,
            contentDescription = "Error",
            modifier = Modifier
                .size(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ComposeTextView.TitleTextView(
            text = errorStrings.first,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        ComposeTextView.TextView(
            text = errorStrings.second,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetryClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.primary))
        ) {
            ComposeTextView.TitleTextView(
                text = "Retry",
                textColor = colorResource(R.color.white),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun FullScreenLoading() {
    ComposeViewUtils.Loading(
        modifier = Modifier.size(40.dp)
    )
}