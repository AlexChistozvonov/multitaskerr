package com.aldera.multitasker.presentation.task.view

import com.aldera.multitasker.data.models.CreateTaskResponse
import com.aldera.multitasker.data.models.TaskResponse

sealed class ViewTaskEvent {
    data class UpdateSubTaskTask(val subTask: List<TaskResponse>?) : ViewTaskEvent()
    object Loading : ViewTaskEvent()
    data class Success(val task: CreateTaskResponse?) : ViewTaskEvent()
    data class Error(val error: Exception?) : ViewTaskEvent()
    object Init : ViewTaskEvent()
}

data class ViewTaskViewState(
    val error: Exception? = null,
    val loading: Boolean = false,
    val subTask: List<TaskResponse>? = null,
    val task: CreateTaskResponse? = null,
    val event: ViewTaskEvent = ViewTaskEvent.Init
) {
    fun applyEvent(event: ViewTaskEvent) = when (event) {
        is ViewTaskEvent.Error -> copy(error = event.error, loading = false, event = event)
        ViewTaskEvent.Init -> copy(event = event)
        ViewTaskEvent.Loading -> copy(loading = true, event = event)
        is ViewTaskEvent.Success -> copy(task = event.task, loading = false, event = event)
        is ViewTaskEvent.UpdateSubTaskTask -> copy(
            subTask = event.subTask,
            loading = false,
            event = event
        )
    }
}
