package com.aldera.multitasker.presentation.task.edit

sealed class EditTaskEvent {
    object Loading : EditTaskEvent()
    object Success : EditTaskEvent()
    data class Error(val error: Exception?) : EditTaskEvent()
    data class TitleChanged(val title: String) : EditTaskEvent()
    data class DescriptionChanged(val description: String) : EditTaskEvent()
    data class DeadlineChanged(val deadline: String) : EditTaskEvent()
    data class Importance(val importance: Int) : EditTaskEvent()
    object Init : EditTaskEvent()
}

data class EditTaskViewState(
    val titleText: String = "",
    val descriptionText: String = "",
    val deadlineText: String = "",
    val importance: Int = 0,
    val error: Exception? = null,
    val loading: Boolean = false,
    val event: EditTaskEvent = EditTaskEvent.Init
) {
    fun applyEvent(event: EditTaskEvent) = when (event) {
        is EditTaskEvent.DeadlineChanged -> copy(deadlineText = event.deadline, event = event)
        is EditTaskEvent.DescriptionChanged -> copy(
            descriptionText = event.description,
            event = event
        )
        is EditTaskEvent.Error -> copy(error = event.error, loading = false, event = event)
        is EditTaskEvent.Importance -> copy(importance = event.importance, event = event)
        EditTaskEvent.Init -> copy(event = event)
        EditTaskEvent.Loading -> copy(loading = true, event = event)
        EditTaskEvent.Success -> copy(loading = false, event = event)
        is EditTaskEvent.TitleChanged -> copy(titleText = event.title, event = event)
    }
}
