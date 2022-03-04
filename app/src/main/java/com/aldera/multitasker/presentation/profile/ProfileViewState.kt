package com.aldera.multitasker.presentation.profile

import com.aldera.multitasker.data.models.UserResponse

sealed class UserEvent {
    object Loading : UserEvent()
    data class Success(val user: UserResponse?) : UserEvent()
    data class Error(val error: Exception?) : UserEvent()
}

data class UserViewState(
    val error: Exception? = null,
    val loading: Boolean = false,
    val loader: Boolean = false,
    val user: UserResponse? = null,
    val event: UserEvent = UserEvent.Loading
) {
    fun applyEvent(event: UserEvent) = when (event) {
        is UserEvent.Error -> copy(error = event.error, event = event)
        UserEvent.Loading -> copy(loading = true, event = event)
        is UserEvent.Success -> copy(user = event.user, loader = true, event = event)
    }
}
