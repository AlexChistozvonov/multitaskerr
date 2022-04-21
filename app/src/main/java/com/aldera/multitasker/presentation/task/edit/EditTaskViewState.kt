package com.aldera.multitasker.presentation.task.edit

import com.aldera.multitasker.data.models.CreateTaskResponse
import com.aldera.multitasker.data.models.UserListResponse

sealed class EditTaskEvent {
    data class EditExecutor(val createTask: CreateTaskResponse?) : EditTaskEvent()
    object Loading : EditTaskEvent()
    object Success : EditTaskEvent()
    data class Error(val error: Exception?) : EditTaskEvent()
    data class TitleChanged(val title: String) : EditTaskEvent()
    data class DescriptionChanged(val description: String) : EditTaskEvent()
    data class DeadlineChanged(val deadline: String) : EditTaskEvent()
    data class Importance(val importance: Int) : EditTaskEvent()
    data class ExecutorEmail(val executorEmail: String) : EditTaskEvent()
    data class GetExecutor(val userList: List<UserListResponse>?) : EditTaskEvent()
}

data class EditTaskViewState(
    val createTask: CreateTaskResponse? = null,
    val titleText: String? = null,
    val descriptionText: String? = null,
    val deadlineText: String? = null,
    val importance: Int? = null,
    val executorEmail: String? = null,
    val userList: List<UserListResponse>? = null,
    val error: Exception? = null,
    val loading: Boolean = false,
    val event: EditTaskEvent = EditTaskEvent.Loading
) {
    fun applyEvent(event: EditTaskEvent) = when (event) {
        is EditTaskEvent.DeadlineChanged -> copy(deadlineText = event.deadline, event = event)
        is EditTaskEvent.DescriptionChanged -> copy(
            descriptionText = event.description,
            event = event
        )
        is EditTaskEvent.Error -> copy(error = event.error, loading = false, event = event)
        is EditTaskEvent.Importance -> copy(importance = event.importance, event = event)
        EditTaskEvent.Loading -> copy(loading = true, event = event)
        EditTaskEvent.Success -> copy(loading = false, event = event)
        is EditTaskEvent.TitleChanged -> copy(titleText = event.title, event = event)
        is EditTaskEvent.ExecutorEmail -> copy(executorEmail = event.executorEmail, event = event)
        is EditTaskEvent.GetExecutor -> copy(
            userList = event.userList,
            loading = false,
            event = event
        )
        is EditTaskEvent.EditExecutor -> copy(createTask = event.createTask, event = event)
    }
}
