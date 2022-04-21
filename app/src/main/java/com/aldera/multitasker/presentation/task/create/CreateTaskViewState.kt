package com.aldera.multitasker.presentation.task.create

import com.aldera.multitasker.data.models.CreateTaskResponse
import com.aldera.multitasker.data.models.UserListResponse

sealed class CreateTaskEvent {
    object Loading : CreateTaskEvent()
    data class Success(val createTask: CreateTaskResponse?) : CreateTaskEvent()
    data class Error(val error: Exception?) : CreateTaskEvent()
    data class GetExecutor(val userList: List<UserListResponse>?) : CreateTaskEvent()
    data class GetExecutorError(val error: Exception?) : CreateTaskEvent()
    data class TitleChanged(val title: String) : CreateTaskEvent()
    data class DescriptionChanged(val description: String) : CreateTaskEvent()
    data class DeadlineChanged(val deadline: String) : CreateTaskEvent()
    data class Importance(val importance: Int) : CreateTaskEvent()
    data class ExecutorEmail(val executorEmail: String) : CreateTaskEvent()
    object Init : CreateTaskEvent()
}

data class CreateTaskViewState(
    val createTask: CreateTaskResponse? = null,
    val titleText: String = "",
    val descriptionText: String = "",
    val deadlineText: String = "",
    val importance: Int = 0,
    val executorId: String = "",
    val userList: List<UserListResponse>? = null,
    val error: Exception? = null,
    val loading: Boolean = false,
    val event: CreateTaskEvent = CreateTaskEvent.Init
) {
    fun applyEvent(event: CreateTaskEvent) = when (event) {
        is CreateTaskEvent.Error -> copy(error = event.error, loading = false, event = event)
        is CreateTaskEvent.GetExecutor -> copy(
            userList = event.userList,
            loading = false,
            event = event
        )
        is CreateTaskEvent.GetExecutorError -> copy(
            error = event.error,
            loading = false,
            event = event
        )
        CreateTaskEvent.Init -> copy(event = event)
        CreateTaskEvent.Loading -> copy(loading = true, event = event)
        is CreateTaskEvent.DeadlineChanged -> copy(deadlineText = event.deadline, event = event)
        is CreateTaskEvent.DescriptionChanged -> copy(
            descriptionText = event.description,
            event = event
        )
        is CreateTaskEvent.Importance -> copy(importance = event.importance, event = event)
        is CreateTaskEvent.TitleChanged -> copy(titleText = event.title, event = event)
        is CreateTaskEvent.ExecutorEmail -> copy(executorId = event.executorEmail, event = event)
        is CreateTaskEvent.Success -> copy(
            loading = false,
            event = event,
            createTask = event.createTask
        )
    }
}
