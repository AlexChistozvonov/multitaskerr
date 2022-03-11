package com.aldera.multitasker.presentation.profile.editEmail

import com.aldera.multitasker.data.models.UserResponse

sealed class EditEmailEvent {
    object Init : EditEmailEvent()
    object Loading : EditEmailEvent()
    data class Success(val user: UserResponse?) : EditEmailEvent()
    object EmailError : EditEmailEvent()
    data class Error(val error: Exception?) : EditEmailEvent()
    data class EmailChanged(val text: String?) : EditEmailEvent()
}

data class EditEmailViewState(
    val error: Exception? = null,
    val loading: Boolean = false,
    val emailText: String? = "",
    val user: UserResponse? = null,
    val emailError: Boolean = false,
    val event: EditEmailEvent = EditEmailEvent.Init
) {
    fun applyEvent(event: EditEmailEvent) = when (event) {
        is EditEmailEvent.Error -> copy(error = event.error, loading = false, event = event)
        EditEmailEvent.Loading -> copy(loading = true, event = event)
        is EditEmailEvent.EmailChanged -> copy(
            emailText = event.text,
            emailError = false,
            event = event
        )
        EditEmailEvent.EmailError -> copy(emailError = true, event = event)
        is EditEmailEvent.Success -> copy(user = event.user, loading = false, event = event)
        EditEmailEvent.Init -> copy(event = event)
    }
}
