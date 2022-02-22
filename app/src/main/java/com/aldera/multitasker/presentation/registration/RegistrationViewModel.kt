package com.aldera.multitasker.presentation.registration

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.registration.RegistrationRepository
import com.aldera.multitasker.ui.util.ConstantRegex
import com.aldera.multitasker.ui.util.PreferencesKey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationRepository: RegistrationRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationViewState())
    val uiState = _uiState.asStateFlow()
    private val loading = MutableLiveData<Boolean>()

    private fun emitEvent(event: RegistrationEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onEmailTextChanged(text: String) {
        emitEvent(RegistrationEvent.EmailChanged(text))
    }

    fun onPasswordTextChanged(text: String, repeatField: Boolean = false) {
        if (repeatField) {
            emitEvent(RegistrationEvent.Password2Changed(text))
        } else {
            emitEvent(RegistrationEvent.PasswordChanged(text))
        }
    }

    fun registration() {
        val regEmail = Regex(ConstantRegex.REGEX_EMAIL)
        val regPassword = Regex(ConstantRegex.REGEX_PASSWORD)
        if (regEmail.matches(_uiState.value.emailText) && regPassword.matches(_uiState.value.passwordText)) {
            viewModelScope.launch {
                val result = registrationRepository.registration(
                    _uiState.value.emailText,
                    _uiState.value.passwordText,
                    _uiState.value.password2Text,
                )
                when (result) {
                    is LoadingResult.Error -> {
                        emitEvent(RegistrationEvent.Error(result.exception))
                    }
                    is LoadingResult.Success -> {
                        loading.postValue(false)
                        Timber.e("Success")
                        sharedPreferences.edit().apply() {
                            putString(PreferencesKey.ACCESS_TOKEN, result.data.accessToken)
                        }.apply()
                    }
                }
            }
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
