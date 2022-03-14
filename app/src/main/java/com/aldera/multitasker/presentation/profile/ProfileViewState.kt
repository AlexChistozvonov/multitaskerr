package com.aldera.multitasker.presentation.profile

import com.aldera.multitasker.data.models.MultitaskerImage
import com.aldera.multitasker.data.models.UserResponse

sealed class UserEvent {
    object Loading : UserEvent()
    data class Success(val user: UserResponse?) : UserEvent()
    data class Error(val error: Exception?) : UserEvent()
    object ImageLoading : UserEvent()
    data class ImageError(val error: Exception?) : UserEvent()
    object ExitLoading : UserEvent()
    data class ExitProfileError(val error: Exception?) : UserEvent()
}

data class UserViewState(
    val error: Exception? = null,
    val loading: Boolean = false,
    val user: UserResponse? = null,
    val image: MultitaskerImage? = null,
    val event: UserEvent = UserEvent.Loading
) {
    fun applyEvent(event: UserEvent) = when (event) {
        is UserEvent.Error -> copy(error = event.error, loading = false, event = event)
        UserEvent.Loading -> copy(loading = true, event = event)
        is UserEvent.Success -> copy(user = event.user, loading = false, event = event)
        UserEvent.ImageLoading -> copy(event = event)
        is UserEvent.ImageError -> copy(event = event, error = event.error, loading = false)
        UserEvent.ExitLoading -> copy(event = event)
        is UserEvent.ExitProfileError -> copy(event = event, error = event.error, loading = false)
    }
}
