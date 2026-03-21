package com.example.bbltripplanner.screens.notification.viewModels

import androidx.lifecycle.viewModelScope
import com.example.bbltripplanner.common.baseClasses.BaseMVIVViewModel
import com.example.bbltripplanner.common.entity.RequestResponseStatus
import com.example.bbltripplanner.common.utils.SafeIOUtil
import com.example.bbltripplanner.screens.notification.model.NotificationModel
import com.example.bbltripplanner.screens.notification.usecases.NotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    val notificationUseCase: NotificationUseCase
): BaseMVIVViewModel<NotificationViewModelIntent.ViewEvent>() {
    private val _notificationsFetchStatus: MutableStateFlow<RequestResponseStatus<List<NotificationModel>>> = MutableStateFlow(RequestResponseStatus())
    val notificationsFetchStatus: StateFlow<RequestResponseStatus<List<NotificationModel>>> = _notificationsFetchStatus.asStateFlow()

    init {
        processEvent(NotificationViewModelIntent.ViewEvent.FetchNotification)
    }

    override fun processEvent(viewEvent: NotificationViewModelIntent.ViewEvent) {
        when (viewEvent) {
            NotificationViewModelIntent.ViewEvent.FetchNotification -> fetchNotification()
        }
    }

    private fun fetchNotification() {
        _notificationsFetchStatus.value = _notificationsFetchStatus.value.copy(isLoading = true)
        viewModelScope.launch {
            val notificationListResult = SafeIOUtil.safeCall {
                notificationUseCase.getNotifications()
            }
            notificationListResult.onSuccess {
                _notificationsFetchStatus.value = _notificationsFetchStatus.value.copy(
                    isLoading = false,
                    data = it
                )
            }
            notificationListResult.onFailure {
                _notificationsFetchStatus.value = _notificationsFetchStatus.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }
}