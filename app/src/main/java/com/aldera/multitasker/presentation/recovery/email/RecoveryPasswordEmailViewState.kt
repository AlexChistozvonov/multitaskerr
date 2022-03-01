package com.aldera.multitasker.presentation.recovery.email

sealed class RecoveryPasswordEmailEvent {
    object Success : RecoveryPasswordEmailEvent()
    object Loading : RecoveryPasswordEmailEvent()
    object EmailError : RecoveryPasswordEmailEvent()
    data class Error(val error: Exception?) : RecoveryPasswordEmailEvent()
    data class EmailChanged(val text: String) : RecoveryPasswordEmailEvent()
    data class Key(val text: String) : RecoveryPasswordEmailEvent()
}

sealed class RecoveryPasswordEmailNavigationEvent {
    data class NextStep(val key: String) : RecoveryPasswordEmailNavigationEvent()
}

data class RecoveryPasswordEmailViewState(
    val key: String = "",
    val error: Exception? = null,
    val emailText: String = "",
    val emailError: Boolean = false,
    val loading: Boolean = false,
    val loader: Boolean = false,
    val event: RecoveryPasswordEmailEvent = RecoveryPasswordEmailEvent.Success
) {
    fun applyEvent(event: RecoveryPasswordEmailEvent) = when (event) {
        is RecoveryPasswordEmailEvent.EmailChanged -> copy(
            emailText = event.text,
            emailError = false,
            event = event
        )
        RecoveryPasswordEmailEvent.EmailError -> copy(emailError = true, event = event)
        is RecoveryPasswordEmailEvent.Error -> copy(error = event.error, event = event)
        RecoveryPasswordEmailEvent.Loading -> copy(loading = true, event = event)
        RecoveryPasswordEmailEvent.Success -> copy(loader = true, event = event, loading = false)
        is RecoveryPasswordEmailEvent.Key -> copy(key = event.text, event = event)
    }
}
