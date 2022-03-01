package com.aldera.multitasker.presentation.recovery.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.recovery.RecoveryPasswordEmailRepository
import com.aldera.multitasker.ui.util.ConstantRegex
import com.aldera.multitasker.ui.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RecoveryPasswordEmailViewModel @Inject constructor(
    private val recoveryPasswordEmailRepository: RecoveryPasswordEmailRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecoveryPasswordEmailViewState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent =
        MutableStateFlow(Event.empty<RecoveryPasswordEmailNavigationEvent>())
    val navigationEvent = _navigationEvent.asStateFlow()

    private fun emitEvent(event: RecoveryPasswordEmailEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onEmailTextChanged(text: String) {
        emitEvent(RecoveryPasswordEmailEvent.EmailChanged(text))
    }

    fun recoveryPasswordEmail() = viewModelScope.launch {
        emitEvent(RecoveryPasswordEmailEvent.Loading)
        val regEmail = Regex(ConstantRegex.REGEX_EMAIL)
        if (regEmail.matches(_uiState.value.emailText)) {
            val result =
                recoveryPasswordEmailRepository.recoveryPasswordEmail(_uiState.value.emailText)
            when (result) {
                is LoadingResult.Error -> {
                    emitEvent(RecoveryPasswordEmailEvent.Error(result.exception))
                }
                is LoadingResult.Success -> {
                    emitEvent(RecoveryPasswordEmailEvent.Success)
                    result.data.key?.let {
                        _navigationEvent.emit(Event(RecoveryPasswordEmailNavigationEvent.NextStep(it)))
                    }
                }
            }
        } else {
            handleError()
        }
    }

    private fun handleError() {
        val regEmail = Regex(ConstantRegex.REGEX_EMAIL)
        if (!regEmail.matches(_uiState.value.emailText)) {
            emitEvent(RecoveryPasswordEmailEvent.EmailError)
        }
    }
}
