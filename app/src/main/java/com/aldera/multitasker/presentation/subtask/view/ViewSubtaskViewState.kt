package com.aldera.multitasker.presentation.subtask.view

import com.aldera.multitasker.data.models.CreateSubtaskResponse

sealed class ViewSubtaskEvent {
    object Loading : ViewSubtaskEvent()
    data class Success(val subTask: CreateSubtaskResponse?) : ViewSubtaskEvent()
    data class Error(val error: Exception?) : ViewSubtaskEvent()
    object Init : ViewSubtaskEvent()
}

data class ViewSubtaskViewState(
    val error: Exception? = null,
    val loading: Boolean = false,
    val subTask: CreateSubtaskResponse? = null,
    val event: ViewSubtaskEvent = ViewSubtaskEvent.Init
) {
    fun applyEvent(event: ViewSubtaskEvent) = when (event) {
        is ViewSubtaskEvent.Error -> copy(
            error = event.error,
            loading = false,
            event = event
        )
        ViewSubtaskEvent.Init -> copy(event = event)
        ViewSubtaskEvent.Loading -> copy(loading = true, event = event)
        is ViewSubtaskEvent.Success -> copy(
            subTask = event.subTask,
            loading = false,
            event = event
        )
    }
}
