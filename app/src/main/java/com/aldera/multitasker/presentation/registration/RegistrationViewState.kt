package com.aldera.multitasker.presentation.registration

import com.aldera.multitasker.presentation.login.LoginEvent

sealed class RegistrationEvent{
    object Loading: RegistrationEvent()
    object PasswordError: RegistrationEvent()
    object EmailError: RegistrationEvent()
    data class EmailChanged(val text: String) : RegistrationEvent()
    data class PasswordChanged(val text: String) : RegistrationEvent()
}

data class RegistrationViewState(
    val emailText: String = "",
    val passwordText: String = "",
    val passwordRepeatText: String = "",
    val passwordError: Boolean = false,
    val emailError: Boolean = false,
    val passwordRepeatError: Boolean = false,
    val loading: Boolean = false,
    val event: RegistrationEvent = RegistrationEvent.Loading
) {
    fun applyEvent(event: RegistrationEvent) = when (event){
        is RegistrationEvent.EmailChanged -> copy(emailText = event.text, emailError = false)
        RegistrationEvent.Loading -> copy(loading = true)
        is RegistrationEvent.PasswordChanged -> copy(emailText = event.text)
        RegistrationEvent.EmailError -> copy(emailError = true)
        RegistrationEvent.PasswordError -> copy()
    }
}