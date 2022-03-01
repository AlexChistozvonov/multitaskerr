package com.aldera.multitasker.presentation.recovery.create

sealed class RecoveryPasswordCreateEvent {
    object Success : RecoveryPasswordCreateEvent()
    object Loading : RecoveryPasswordCreateEvent()
    object PasswordError : RecoveryPasswordCreateEvent()
    data class Error(val error: Exception?) : RecoveryPasswordCreateEvent()
    data class PasswordChanged(val text: String) : RecoveryPasswordCreateEvent()
    data class Password2Changed(val text: String) : RecoveryPasswordCreateEvent()
}

sealed class RecoveryPasswordCreateNavigationEvent {
    data class NextStep(val key: String) : RecoveryPasswordCreateNavigationEvent()
}

data class RecoveryPasswordCreateViewState(
    val error: Exception? = null,
    val passwordText: String = "",
    val password2Text: String = "",
    val passwordError: Boolean = false,
    val loading: Boolean = false,
    val loader: Boolean = false,
    val event: RecoveryPasswordCreateEvent = RecoveryPasswordCreateEvent.Success
) {

    fun applyEvent(event: RecoveryPasswordCreateEvent) = when (event) {
        is RecoveryPasswordCreateEvent.Error -> copy(error = event.error, event = event)
        RecoveryPasswordCreateEvent.Loading -> copy(loading = true, event = event)
        is RecoveryPasswordCreateEvent.Password2Changed -> copy(
            password2Text = event.text,
            event = event
        )
        is RecoveryPasswordCreateEvent.PasswordChanged -> copy(
            passwordText = event.text,
            event = event
        )
        RecoveryPasswordCreateEvent.PasswordError -> copy(passwordError = true, event = event)
        RecoveryPasswordCreateEvent.Success -> copy(loader = true, event = event, loading = false)
    }
}
