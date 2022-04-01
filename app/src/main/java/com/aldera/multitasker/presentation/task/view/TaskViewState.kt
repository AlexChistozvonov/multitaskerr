package com.aldera.multitasker.presentation.task.view

import com.aldera.multitasker.data.models.TaskResponse

sealed class TaskEvent {
    object Loading : TaskEvent()
    data class Success(val task: List<TaskResponse>?) : TaskEvent()
    data class Error(val error: Exception?) : TaskEvent()
    object Init : TaskEvent()
}

data class TaskViewState(
    val error: Exception? = null,
    val loading: Boolean = false,
    val task: List<TaskResponse>? = null,
    val event: TaskEvent = TaskEvent.Init
) {
    fun applyEvent(event: TaskEvent) = when (event) {
        is TaskEvent.Error -> copy(error = event.error, loading = false, event = event)
        TaskEvent.Loading -> copy(loading = true, event = event)
        is TaskEvent.Success -> copy(task = event.task, loading = false, event = event)
        TaskEvent.Init -> copy(event = event)
    }
}
