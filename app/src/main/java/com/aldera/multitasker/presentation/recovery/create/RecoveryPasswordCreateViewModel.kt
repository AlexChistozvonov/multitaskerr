package com.aldera.multitasker.presentation.recovery.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.recovery.create.RecoveryPasswordCreateRepositoryImpl
import com.aldera.multitasker.ui.util.ConstantRegex
import com.aldera.multitasker.ui.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RecoveryPasswordCreateViewModel @Inject constructor(
    private val recoveryPasswordCreateRepository: RecoveryPasswordCreateRepositoryImpl,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecoveryPasswordCreateViewState())
    val uiState = _uiState.asStateFlow()
    private fun emitEvent(event: RecoveryPasswordCreateEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    private val _navigationEvent =
        MutableStateFlow(Event.empty<RecoveryPasswordCreateNavigationEvent>())
    val navigationEvent = _navigationEvent.asStateFlow()

    fun onPasswordTextChanged(text: String, repeatField: Boolean = false) {
        if (repeatField) {
            emitEvent(RecoveryPasswordCreateEvent.Password2Changed(text))
        } else {
            emitEvent(RecoveryPasswordCreateEvent.PasswordChanged(text))
        }
    }

    fun recoveryPasswordCreate(key: String) = viewModelScope.launch {
        emitEvent(RecoveryPasswordCreateEvent.Loading)
        val regPassword = Regex(ConstantRegex.REGEX_PASSWORD)
        if (regPassword.matches(_uiState.value.passwordText)) {
            when (val result = recoveryPasswordCreateRepository.recoveryPasswordCreate(
                _uiState.value.passwordText,
                _uiState.value.password2Text,
                key
            )) {
                is LoadingResult.Error -> {
                    emitEvent(RecoveryPasswordCreateEvent.Error(result.exception))
                }
                is LoadingResult.Success -> {
                    emitEvent(RecoveryPasswordCreateEvent.Success)
                }
            }
        } else {
            handleError()
        }
    }

    private fun handleError() {
        val regPassword = Regex(ConstantRegex.REGEX_PASSWORD)
        if (!regPassword.matches(_uiState.value.passwordText)) {
            emitEvent(RecoveryPasswordCreateEvent.PasswordError)
        }
    }
}
