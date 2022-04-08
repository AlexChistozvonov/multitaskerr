package com.aldera.multitasker.presentation.subtask.edit

sealed class EditSubtaskEvent {
    object Loading : EditSubtaskEvent()
    object Success : EditSubtaskEvent()
    data class Error(val error: Exception?) : EditSubtaskEvent()
    data class TitleChanged(val title: String) : EditSubtaskEvent()
    data class DescriptionChanged(val description: String) : EditSubtaskEvent()
    data class DeadlineChanged(val deadline: String) : EditSubtaskEvent()
    data class Importance(val importance: Int) : EditSubtaskEvent()
    object Init : EditSubtaskEvent()
}

data class EditSubtaskViewState(
    val titleText: String = "",
    val descriptionText: String = "",
    val deadlineText: String = "",
    val importance: Int = 0,
    val error: Exception? = null,
    val loading: Boolean = false,
    val event: EditSubtaskEvent = EditSubtaskEvent.Init
) {
    fun applyEvent(event: EditSubtaskEvent) = when (event) {
        is EditSubtaskEvent.DeadlineChanged -> copy(deadlineText = event.deadline, event = event)
        is EditSubtaskEvent.DescriptionChanged -> copy(
            descriptionText = event.description,
            event = event
        )
        is EditSubtaskEvent.Error -> copy(error = event.error, loading = false, event = event)
        is EditSubtaskEvent.Importance -> copy(importance = event.importance, event = event)
        EditSubtaskEvent.Init -> copy(event = event)
        EditSubtaskEvent.Loading -> copy(loading = true, event = event)
        EditSubtaskEvent.Success -> copy(loading = false, event = event)
        is EditSubtaskEvent.TitleChanged -> copy(titleText = event.title, event = event)
    }
}
