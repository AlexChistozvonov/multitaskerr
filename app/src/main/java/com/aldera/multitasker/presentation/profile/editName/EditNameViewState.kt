package com.aldera.multitasker.presentation.profile.editName

import com.aldera.multitasker.data.models.UserResponse

sealed class EditNameEvent {
    object Init : EditNameEvent()
    object Loading : EditNameEvent()
    data class Success(val user: UserResponse?) : EditNameEvent()
    object NameError : EditNameEvent()
    data class Error(val error: Exception?) : EditNameEvent()
    data class NameChanged(val text: String?) : EditNameEvent()
}

data class EditNameViewState(
    val error: Exception? = null,
    val loading: Boolean = false,
    val nameText: String? = "",
    val user: UserResponse? = null,
    val nameError: Boolean = false,
    val event: EditNameEvent = EditNameEvent.Init
) {
    fun applyEvent(event: EditNameEvent) = when (event) {
        is EditNameEvent.Error -> copy(error = event.error, loading = false, event = event)
        EditNameEvent.Loading -> copy(loading = true, event = event)
        is EditNameEvent.NameChanged -> copy(
            nameText = event.text,
            nameError = false,
            event = event
        )
        EditNameEvent.NameError -> copy(nameError = true, event = event)
        is EditNameEvent.Success -> copy(user = event.user, loading = false, event = event)
        EditNameEvent.Init -> copy(event = event)
    }
}
