package com.aldera.multitasker.presentation.recovery.code

sealed class RecoveryPasswordCodeEvent {
    object Success : RecoveryPasswordCodeEvent()
    object CodeError : RecoveryPasswordCodeEvent()
    object Loading : RecoveryPasswordCodeEvent()
    data class Error(val error: Exception?) : RecoveryPasswordCodeEvent()
    data class CodeChange(val text: String) : RecoveryPasswordCodeEvent()
    data class Key(val text: String) : RecoveryPasswordCodeEvent()
}

sealed class RecoveryPasswordCodeNavigationEvent {
    data class NextStep(val key: String) : RecoveryPasswordCodeNavigationEvent()
}

data class RecoveryPasswordCodeViewState(
    val key: String = "",
    val code: String = "",
    val codeError: Boolean = false,
    val error: Exception? = null,
    val loading: Boolean = false,
    val loader: Boolean = false,
    val event: RecoveryPasswordCodeEvent = RecoveryPasswordCodeEvent.Success
) {
    fun applyEvent(event: RecoveryPasswordCodeEvent) = when (event) {
        is RecoveryPasswordCodeEvent.Error -> copy(error = event.error, event = event)
        RecoveryPasswordCodeEvent.Loading -> copy(loading = true, event = event)
        is RecoveryPasswordCodeEvent.CodeChange -> copy(
            code = event.text,
            codeError = false,
            event = event
        )
        RecoveryPasswordCodeEvent.CodeError -> copy(codeError = true, event = event)
        RecoveryPasswordCodeEvent.Success -> copy(loader = true, event = event, loading = false)
        is RecoveryPasswordCodeEvent.Key -> copy(key = event.text, codeError = false, event = event)
    }
}
