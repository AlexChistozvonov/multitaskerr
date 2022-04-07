package com.aldera.multitasker.presentation.subtask.create

import com.aldera.multitasker.data.models.UserTaskResponse

sealed class CreateSubtaskEvent {
    object Loading : CreateSubtaskEvent()
    object Success : CreateSubtaskEvent()
    data class Error(val error: Exception?) : CreateSubtaskEvent()
    data class GetExecutor(val userTask: List<UserTaskResponse>?) : CreateSubtaskEvent()
    data class GetExecutorError(val error: Exception?) : CreateSubtaskEvent()
    data class TitleChanged(val title: String) : CreateSubtaskEvent()
    data class DescriptionChanged(val description: String) : CreateSubtaskEvent()
    data class DeadlineChanged(val deadline: String) : CreateSubtaskEvent()
    data class Importance(val importance: Int) : CreateSubtaskEvent()
    data class PerformerId(val performerId: String) : CreateSubtaskEvent()
    object Init : CreateSubtaskEvent()
}

data class CreateSubtaskViewState(
    val titleText: String = "",
    val descriptionText: String = "",
    val deadlineText: String = "",
    val importance: Int = 0,
    val performerId: String = "",
    val userTask: List<UserTaskResponse>? = null,
    val error: Exception? = null,
    val loading: Boolean = false,
    val event: CreateSubtaskEvent = CreateSubtaskEvent.Init
) {
    fun applyEvent(event: CreateSubtaskEvent) = when (event) {
        is CreateSubtaskEvent.Error -> copy(error = event.error, loading = false, event = event)
        is CreateSubtaskEvent.GetExecutor -> copy(
            userTask = event.userTask,
            loading = false,
            event = event
        )
        is CreateSubtaskEvent.GetExecutorError -> copy(
            error = event.error,
            loading = false,
            event = event
        )
        CreateSubtaskEvent.Init -> copy(event = event)
        CreateSubtaskEvent.Loading -> copy(loading = true, event = event)
        CreateSubtaskEvent.Success -> copy(loading = false, event = event)
        is CreateSubtaskEvent.DeadlineChanged -> copy(deadlineText = event.deadline, event = event)
        is CreateSubtaskEvent.DescriptionChanged -> copy(
            descriptionText = event.description,
            event = event
        )
        is CreateSubtaskEvent.Importance -> copy(importance = event.importance, event = event)
        is CreateSubtaskEvent.TitleChanged -> copy(titleText = event.title, event = event)
        is CreateSubtaskEvent.PerformerId -> copy(performerId = event.performerId, event = event)
    }
}
