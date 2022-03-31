package com.aldera.multitasker.presentation.project.create

sealed class CreateProjectEvent {
    object Success : CreateProjectEvent()
    object Loading : CreateProjectEvent()
    object Init : CreateProjectEvent()
    data class Error(val error: Exception?) : CreateProjectEvent()
    data class TitleChanged(val text: String) : CreateProjectEvent()
}

data class CreateProjectViewState(
    val error: Exception? = null,
    val titleText: String = "",
    val loading: Boolean = false,
    val loader: Boolean = false,
    val event: CreateProjectEvent = CreateProjectEvent.Init
) {
    fun applyEvent(event: CreateProjectEvent) = when (event) {
        is CreateProjectEvent.Error -> copy(error = event.error, event = event)
        CreateProjectEvent.Init -> copy(event = event)
        CreateProjectEvent.Loading -> copy(loader = true, event = event)
        CreateProjectEvent.Success -> copy(loader = true, event = event)
        is CreateProjectEvent.TitleChanged -> copy(titleText = event.text, event = event)
    }
}
