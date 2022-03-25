package com.aldera.multitasker.presentation.project.edit

sealed class EditProjectEvent {
    object Success : EditProjectEvent()
    object Loading : EditProjectEvent()
    object Init : EditProjectEvent()
    data class Error(val error: Exception?) : EditProjectEvent()
    data class TitleChanged(val text: String) : EditProjectEvent()
}

data class EditProjectViewState(
    val categoryId: String? = null,
    val error: Exception? = null,
    val titleText: String = "",
    val loading: Boolean = false,
    val loader: Boolean = false,
    val event: EditProjectEvent = EditProjectEvent.Init
) {
    fun applyEvent(event: EditProjectEvent) = when (event) {
        is EditProjectEvent.Error -> copy(error = event.error, event = event)
        EditProjectEvent.Init -> copy(event = event)
        EditProjectEvent.Loading -> copy(loader = true, event = event)
        EditProjectEvent.Success -> copy(loader = true, event = event)
        is EditProjectEvent.TitleChanged -> copy(titleText = event.text, event = event)
    }
}
