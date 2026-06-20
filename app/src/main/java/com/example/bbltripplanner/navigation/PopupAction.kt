package com.example.bbltripplanner.navigation

sealed interface PopupAction {
    data class ShowPopup(
        val titleRes: Int? = null,
        val messageRes: Int? = null,
        val onConfirm: (() -> Unit)? = null,
        val onCancel: (() -> Unit)? = null,
        val confirmButtonTextRes: Int? = null,
        val cancelButtonTextRes: Int? = null,
        val isCancellable: Boolean = true,
        val isConfirmPositive: Boolean = true
    ) : PopupAction
}