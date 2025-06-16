package com.example.bbltripplanner.common.baseClasses

interface BaseMVIVPresenter<ViewEvent> {
    fun processEvent(viewEvent: ViewEvent)
}