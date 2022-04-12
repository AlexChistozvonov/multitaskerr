package com.aldera.multitasker.presentation.appointed

import com.aldera.multitasker.data.models.TaskCountResponse
import com.aldera.multitasker.data.models.UserTaskResponse

sealed class AppointedEvent {
    object Loading : AppointedEvent()
    data class Success(val userTask: List<UserTaskResponse>?) : AppointedEvent()
    data class GetUserSubtask(val userSubtask: List<UserTaskResponse>?) : AppointedEvent()
    data class GetTaskCount(val taskCount: TaskCountResponse?) : AppointedEvent()
    data class Error(val error: Exception?) : AppointedEvent()
    data class Id(val text: String) : AppointedEvent()
}

data class AppointedViewState(
    val id: String? = null,
    val error: Exception? = null,
    val loading: Boolean = false,
    val taskCount: TaskCountResponse? = null,
    val userTask: List<UserTaskResponse>? = null,
    val userSubtask: List<UserTaskResponse>? = null,
    val event: AppointedEvent = AppointedEvent.Loading
) {
    fun applyEvent(event: AppointedEvent) = when (event) {
        is AppointedEvent.Error -> copy(error = event.error, loading = false, event = event)
        is AppointedEvent.Id -> copy(id = event.text, event = event)
        AppointedEvent.Loading -> copy(loading = true, event = event)
        is AppointedEvent.Success -> copy(userTask = event.userTask, loading = false, event = event)
        is AppointedEvent.GetUserSubtask -> copy(
            userSubtask = event.userSubtask,
            loading = false,
            event = event
        )
        is AppointedEvent.GetTaskCount -> copy(
            taskCount = event.taskCount,
            loading = false,
            event = event
        )
    }
}
