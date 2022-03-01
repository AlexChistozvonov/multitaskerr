package com.aldera.multitasker.presentation.recovery.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.recovery.code.RecoveryPasswordCodeRepositoryImpl
import com.aldera.multitasker.ui.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RecoveryPasswordCodeViewModel @Inject constructor(
    private val recoveryPasswordCodeRepository: RecoveryPasswordCodeRepositoryImpl,
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(RecoveryPasswordCodeViewState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent =
        MutableStateFlow(Event.empty<RecoveryPasswordCodeNavigationEvent>())
    val navigationEvent = _navigationEvent.asStateFlow()

    private fun emitEvent(event: RecoveryPasswordCodeEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onCodeTextChanged(text: String) {
        emitEvent(RecoveryPasswordCodeEvent.CodeChange(text))
    }

    fun recoveryPasswordCode(key: String) = viewModelScope.launch {
        emitEvent(RecoveryPasswordCodeEvent.Loading)
        when (val result =
            recoveryPasswordCodeRepository.recoveryPasswordCode(
                _uiState.value.code,
                key
            )) {
            is LoadingResult.Error -> {
                emitEvent(RecoveryPasswordCodeEvent.Error(result.exception))
            }
            is LoadingResult.Success -> {
                emitEvent(RecoveryPasswordCodeEvent.Success)
                result.data.key.let {
                    _navigationEvent.emit(Event(RecoveryPasswordCodeNavigationEvent.NextStep(it.toString())))
                }
            }
        }
    }
}
