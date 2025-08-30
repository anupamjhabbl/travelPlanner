package com.example.bbltripplanner.screens.home.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVPresenter
import com.example.bbltripplanner.screens.home.entities.CxeResponseError
import com.example.bbltripplanner.screens.home.entities.HomeCxeResponse
import com.example.bbltripplanner.screens.home.entities.HomeCxeWidget
import com.example.bbltripplanner.screens.home.usecases.HomeCxeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class HomeExperienceViewModel(
    private val homeCxeUseCase: HomeCxeUseCase
) : ViewModel(), BaseMVIVPresenter<HomeExperienceIntent.ViewEvent> {

    private val _widgetsLiveData: MutableStateFlow<List<HomeCxeWidget>> = MutableStateFlow(emptyList())
    val widgetsLiveData: StateFlow<List<HomeCxeWidget>> = _widgetsLiveData

    private val _viewEffectLiveData: MutableSharedFlow<HomeExperienceIntent.ViewEffect> = MutableSharedFlow()
    val viewEffectLiveData: SharedFlow<HomeExperienceIntent.ViewEffect> = _viewEffectLiveData

    private val _viewStateLiveData: MutableStateFlow<HomeExperienceIntent.ViewState?> = MutableStateFlow(null)
    val viewStateLiveData: StateFlow<HomeExperienceIntent.ViewState?> = _viewStateLiveData

    override fun processEvent(viewEvent: HomeExperienceIntent.ViewEvent) {
        when (viewEvent) {
            HomeExperienceIntent.ViewEvent.Initialize -> init()
        }
    }

    private fun init() {
        clearCxeWidgets()
    }

    private fun clearCxeWidgets() {
        _widgetsLiveData.value = emptyList()
        getCxeResponse()
    }

    private fun getCxeResponse() {
        viewModelScope.launch {
            _viewStateLiveData.value =  HomeExperienceIntent.ViewState.ShowFullScreenLoading
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    homeCxeUseCase.getHomeCxeResponse()
                }
            }
            result.onSuccess { response ->
                 processResponse(response)
            }.onFailure { exception ->
                if (exception is IOException) {
                    _viewStateLiveData.value = HomeExperienceIntent.ViewState.ShowCxeResponseError(CxeResponseError.INTERNET_ERROR)
                } else {
                    _viewStateLiveData.value = HomeExperienceIntent.ViewState.ShowCxeResponseError(CxeResponseError.SERVER_ERROR)
                }
            }
        }
    }

    private fun processResponse(response: HomeCxeResponse) {
        val widgetList = response.sections.firstOrNull()?.widgets
        fetchBundleData(widgetList)
        if (widgetList == null) {
            _viewStateLiveData.value = HomeExperienceIntent.ViewState.ShowCxeResponseError(CxeResponseError.NO_DATA_ERROR)
        } else {
            _widgetsLiveData.value = widgetList
            _viewStateLiveData.value = null
        }
    }

    private fun fetchBundleData(widgetList: List<HomeCxeWidget>?) {
        widgetList?.forEachIndexed { index, widget ->
            when(widget) {
                is HomeCxeWidget.GreetingWidget, is HomeCxeWidget.ImageCarouselWidget, is HomeCxeWidget.NewsBannerWidget, is HomeCxeWidget.TopPicksByLocationCtaWidget -> {}
                is HomeCxeWidget.TravelThreadsBundleWidget -> getTravelThreadBundleData(widget.data.content?.bundleData, index)
                is HomeCxeWidget.UserTripBundleWidget -> getUserTripBundleData(widget.data.content?.bundleData, index)
                is HomeCxeWidget.BundleItemsWidget -> getBundleWidgetData(widget.data.content?.bundleData, index)
            }
        }
    }

    private fun getBundleWidgetData(bundleData: String?, index: Int) {
        if (bundleData.isNullOrEmpty()) {
            return
        }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    homeCxeUseCase.getBundleWidgetData(bundleData)
                }
            }
            result.onSuccess { newData ->
                if (newData.isNotEmpty()) {
                    _widgetsLiveData.update { currentList ->
                        currentList.toMutableList().apply {
                            val widget = this[index]
                            if (widget is HomeCxeWidget.BundleItemsWidget) {
                                this[index] = widget.copy(
                                    data = widget.data.copy(widgetList = newData)
                                )
                            }
                        }
                    }
                }
            }
            result.onFailure {
                Log.d("ELLOR", it.message.toString() )
            }
        }
    }

    private fun getUserTripBundleData(bundleData: String?, index: Int) {
        if (bundleData.isNullOrEmpty()) {
            return
        }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    homeCxeUseCase.getUserTripBundleData(bundleData)
                }
            }
            result.onSuccess { newData ->
                if (newData.isNotEmpty()) {
                    _widgetsLiveData.update { currentList ->
                        currentList.toMutableList().apply {
                            val widget = this[index]
                            if (widget is HomeCxeWidget.UserTripBundleWidget) {
                                this[index] = widget.copy(
                                    data = widget.data.copy(widgetList = newData)
                                )
                            }
                        }
                    }
                }
            }
            result.onFailure {
                Log.d("ELLOR", it.message.toString() )
            }
        }
    }

    private fun getTravelThreadBundleData(bundleData: String?, index: Int) {
        if (bundleData.isNullOrEmpty()) {
            return
        }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    homeCxeUseCase.getTravelThreadBundleData(bundleData)
                }
            }
            result.onSuccess { newData ->
                if (newData.isNotEmpty()) {
                    _widgetsLiveData.update { currentList ->
                        currentList.toMutableList().apply {
                            val widget = this[index]
                            if (widget is HomeCxeWidget.TravelThreadsBundleWidget) {
                                this[index] = widget.copy(
                                    data = widget.data.copy(widgetList = newData)
                                )
                            }
                        }
                    }
                }
            }
            result.onFailure {
                Log.d("ELLOR", it.message.toString() )
            }
        }
    }
}