package com.example.bbltripplanner.common.baseClasses

import androidx.lifecycle.ViewModel

abstract class BaseMVIVViewModel<ViewEvent>: ViewModel() {
    abstract fun processEvent(viewEvent: ViewEvent)
}