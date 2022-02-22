package com.aldera.multitasker.presentation.registration

sealed class RegistrationEvent {
    object Loading : RegistrationEvent()
    object PasswordError : RegistrationEvent()
    object EmailError : RegistrationEvent()
    data class EmailChanged(val text: String) : RegistrationEvent()
    data class Error(val error: Exception?) : RegistrationEvent()
    data class PasswordChanged(val text: String) : RegistrationEvent()
    data class Password2Changed(val text: String) : RegistrationEvent()
}

data class RegistrationViewState(
    val error: Exception? = null,
    val emailText: String = "",
    val passwordText: String = "",
    val password2Text: String = "",
    val passwordError: Boolean = false,
    val emailError: Boolean = false,
    val loading: Boolean = false,
    val event: RegistrationEvent = RegistrationEvent.Loading
) {
    fun applyEvent(event: RegistrationEvent) = when (event) {
        is RegistrationEvent.EmailChanged -> copy(
            emailText = event.text,
            emailError = false,
            event = event
        )
        RegistrationEvent.Loading -> copy(loading = true, event = event)
        is RegistrationEvent.PasswordChanged -> copy(passwordText = event.text, event = event)
        RegistrationEvent.EmailError -> copy(emailError = true, event = event)
        RegistrationEvent.PasswordError -> copy(passwordError = true, event = event)
        is RegistrationEvent.Password2Changed -> copy(password2Text = event.text, event = event)
        is RegistrationEvent.Error -> copy(error = event.error, event = event)
    }
}
