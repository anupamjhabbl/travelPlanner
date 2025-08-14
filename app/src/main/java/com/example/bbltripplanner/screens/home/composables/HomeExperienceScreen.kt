package com.example.bbltripplanner.screens.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.bbltripplanner.screens.home.composables.widgets.HomeBundleItemComposable
import com.example.bbltripplanner.screens.home.composables.widgets.HomeGreetingComposable
import com.example.bbltripplanner.screens.home.composables.widgets.HomeImageCarouselComposable
import com.example.bbltripplanner.screens.home.composables.widgets.HomeLocationFeedCtaComposable
import com.example.bbltripplanner.screens.home.composables.widgets.HomeNewsBannerComposable
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
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        when (val state =  viewState) {
            HomeExperienceIntent.ViewState.ShowFullScreenLoading -> FullScreenLoading()
            is HomeExperienceIntent.ViewState.ShowCxeResponseError -> ShowCxeResponseErrorComposable(state.error)
            else -> ShowItems(widgets, widgetsListState)
        }
    }
}

@Composable
fun ShowItems(widgets: List<HomeCxeWidget>, widgetsListState: LazyListState) {
    LazyColumn(
        state = widgetsListState
    ) {
        items(widgets) { homeCxeWidget ->
            when (homeCxeWidget) {
                is HomeCxeWidget.GreetingWidget -> HomeGreetingComposable(homeCxeWidget)
                is  HomeCxeWidget.ImageCarouselWidget -> HomeImageCarouselComposable(homeCxeWidget)
                is HomeCxeWidget.BundleItemsWidget -> HomeBundleItemComposable(homeCxeWidget)
                is HomeCxeWidget.NewsBannerWidget -> HomeNewsBannerComposable(homeCxeWidget)
                is HomeCxeWidget.TopPicksByLocationCtaWidget -> HomeLocationFeedCtaComposable(homeCxeWidget)
            }
        }
    }
}

@Composable
fun ShowCxeResponseErrorComposable(error: CxeResponseError) {
    Text("Error")
}

@Composable
fun FullScreenLoading() {
    Text("Loaadimg")
}