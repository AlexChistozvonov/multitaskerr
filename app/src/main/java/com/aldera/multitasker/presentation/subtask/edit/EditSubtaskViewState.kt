package com.aldera.multitasker.presentation.subtask.edit

import com.aldera.multitasker.data.models.CreateSubtaskResponse
import com.aldera.multitasker.data.models.UserListResponse

sealed class EditSubtaskEvent {
    data class EditExecutor(val createTask: CreateSubtaskResponse?) : EditSubtaskEvent()
    object Loading : EditSubtaskEvent()
    object Success : EditSubtaskEvent()
    data class Error(val error: Exception?) : EditSubtaskEvent()
    data class TitleChanged(val title: String) : EditSubtaskEvent()
    data class DescriptionChanged(val description: String) : EditSubtaskEvent()
    data class DeadlineChanged(val deadline: String) : EditSubtaskEvent()
    data class Importance(val importance: Int) : EditSubtaskEvent()
    data class ExecutorEmail(val executorEmail: String) : EditSubtaskEvent()
    data class GetExecutor(val userList: List<UserListResponse>?) : EditSubtaskEvent()
}

data class EditSubtaskViewState(
    val createSubtask: CreateSubtaskResponse? = null,
    val titleText: String? = null,
    val descriptionText: String? = null,
    val executorEmail: String? = null,
    val deadlineText: String = "",
    val importance: Int = 0,
    val userList: List<UserListResponse>? = null,
    val error: Exception? = null,
    val loading: Boolean = false,
    val event: EditSubtaskEvent = EditSubtaskEvent.Loading
) {
    fun applyEvent(event: EditSubtaskEvent) = when (event) {
        is EditSubtaskEvent.DeadlineChanged -> copy(deadlineText = event.deadline, event = event)
        is EditSubtaskEvent.DescriptionChanged -> copy(
            descriptionText = event.description,
            event = event
        )
        is EditSubtaskEvent.Error -> copy(error = event.error, loading = false, event = event)
        is EditSubtaskEvent.Importance -> copy(importance = event.importance, event = event)
        EditSubtaskEvent.Loading -> copy(loading = true, event = event)
        EditSubtaskEvent.Success -> copy(loading = false, event = event)
        is EditSubtaskEvent.TitleChanged -> copy(titleText = event.title, event = event)
        is EditSubtaskEvent.EditExecutor -> copy(createSubtask = event.createTask, event = event)
        is EditSubtaskEvent.ExecutorEmail -> copy(
            executorEmail = event.executorEmail,
            event = event
        )
        is EditSubtaskEvent.GetExecutor -> copy(
            userList = event.userList,
            loading = false,
            event = event
        )
    }
}
