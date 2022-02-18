package com.aldera.multitasker.presentation.registration

import androidx.lifecycle.ViewModel
import com.aldera.multitasker.ConstantRegex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: RegistrationEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun registration() {
        val regEmail = Regex(ConstantRegex.REGEX_EMAIL)
        val regPassword = Regex(ConstantRegex.REGEX_PASSWORD)
        if (regEmail.matches(_uiState.value.emailText) && regPassword.matches(_uiState.value.passwordText)) {
            // repository.registration(email, password, repeatPassword)
        } else {
            handleError()
        }
    }

    private fun handleError() {
        val regEmail = Regex(ConstantRegex.REGEX_EMAIL)
        val regPassword = Regex(ConstantRegex.REGEX_PASSWORD)

        if (!regEmail.matches(_uiState.value.emailText)) {
            emitEvent(RegistrationEvent.EmailError)
        }
        if (!regPassword.matches(_uiState.value.passwordText)) {
            emitEvent(RegistrationEvent.PasswordError)
        }
    }
}