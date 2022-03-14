package com.aldera.multitasker.presentation.editPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.editPassword.EditPasswordRepository
import com.aldera.multitasker.ui.util.ConstantRegex
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditPasswordViewModel @Inject constructor(private val editPasswordRepository: EditPasswordRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(EditPasswordViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: EditPasswordEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onNewPasswordTextChanged(text: String) {
        emitEvent(EditPasswordEvent.NewPasswordChanged(text))
    }

    fun onOldPasswordTextChanged(text: String) {
        emitEvent(EditPasswordEvent.OldPasswordChanged(text))
    }

    fun editPassword() {
        val regPassword = Regex(ConstantRegex.REGEX_PASSWORD)
        if (regPassword.matches(_uiState.value.newPasswordText) && regPassword.matches(_uiState.value.oldPasswordText)) {
            emitEvent(EditPasswordEvent.Loading)
            viewModelScope.launch {

                when (val result = editPasswordRepository.editPassword(
                    _uiState.value.oldPasswordText,
                    _uiState.value.newPasswordText,
                )) {
                    is LoadingResult.Error -> emitEvent(EditPasswordEvent.Error(result.exception))
                    is LoadingResult.Success -> emitEvent(EditPasswordEvent.Success)
                }
            }
        } else {
            handlerError()
        }
    }

    private fun handlerError() {
        val regPassword = Regex(ConstantRegex.REGEX_PASSWORD)
        if (!regPassword.matches(_uiState.value.oldPasswordText)) {
            emitEvent(EditPasswordEvent.OldPasswordError)
        }
        if (!regPassword.matches(_uiState.value.newPasswordText)) {
            emitEvent(EditPasswordEvent.NewPasswordError)
        }
    }
}
