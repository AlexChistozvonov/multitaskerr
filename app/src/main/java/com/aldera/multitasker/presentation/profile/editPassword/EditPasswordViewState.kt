package com.aldera.multitasker.presentation.editPassword

sealed class EditPasswordEvent {
    object Init : EditPasswordEvent()
    object Success : EditPasswordEvent()
    object Loading : EditPasswordEvent()
    object NewPasswordError : EditPasswordEvent()
    object OldPasswordError : EditPasswordEvent()
    data class Error(val error: Exception?) : EditPasswordEvent()
    data class NewPasswordChanged(val text: String) : EditPasswordEvent()
    data class OldPasswordChanged(val text: String) : EditPasswordEvent()
}

data class EditPasswordViewState(
    val error: Exception? = null,
    val newPasswordText: String = "",
    val oldPasswordText: String = "",
    val oldPasswordError: Boolean = false,
    val newPasswordError: Boolean = false,
    val loading: Boolean = false,
    val loader: Boolean = false,
    val event: EditPasswordEvent = EditPasswordEvent.Init
) {
    fun applyEvent(event: EditPasswordEvent) = when (event) {
        is EditPasswordEvent.Error -> copy(error = event.error, event = event)
        EditPasswordEvent.Loading -> copy(loading = true, event = event)
        is EditPasswordEvent.NewPasswordChanged -> copy(newPasswordText = event.text, event = event)
        EditPasswordEvent.NewPasswordError -> copy(newPasswordError = true, event = event)
        is EditPasswordEvent.OldPasswordChanged -> copy(oldPasswordText = event.text, event = event)
        EditPasswordEvent.OldPasswordError -> copy(oldPasswordError = true, event = event)
        EditPasswordEvent.Success -> copy(loader = true, event = event, loading = false)
        EditPasswordEvent.Init -> copy(event = event)
    }
}
