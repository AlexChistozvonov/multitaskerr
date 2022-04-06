package com.aldera.multitasker.presentation.task.view

import com.aldera.multitasker.data.models.CreateTaskResponse
import com.aldera.multitasker.data.models.TaskResponse

sealed class ViewTaskViewEvent {
    data class UpdateSubTask(val subTask: List<TaskResponse>?) : ViewTaskViewEvent()
    object Loading : ViewTaskViewEvent()
    data class Success(val task: CreateTaskResponse?) : ViewTaskViewEvent()
    data class Error(val error: Exception?) : ViewTaskViewEvent()
    object Init : ViewTaskViewEvent()
}

data class ViewTaskViewState(
    val error: Exception? = null,
    val loading: Boolean = false,
    val subTask: List<TaskResponse>? = null,
    val task: CreateTaskResponse? = null,
    val event: ViewTaskViewEvent = ViewTaskViewEvent.Init
) {
    fun applyEvent(event: ViewTaskViewEvent) = when (event) {
        is ViewTaskViewEvent.Error -> copy(error = event.error, loading = false, event = event)
        ViewTaskViewEvent.Init -> copy(event = event)
        ViewTaskViewEvent.Loading -> copy(loading = true, event = event)
        is ViewTaskViewEvent.Success -> copy(task = event.task, loading = false, event = event)
        is ViewTaskViewEvent.UpdateSubTask -> copy(
            subTask = event.subTask,
            loading = false,
            event = event
        )
    }
}
