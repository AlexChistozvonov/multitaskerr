package com.aldera.multitasker.presentation.login

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.ConstantRegex
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginViewState())
    val uiState = _uiState.asStateFlow()
    private val loading = MutableLiveData<Boolean>()

    private fun emitEvent(event: LoginEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onEmailTextChanged(text: String) {
        emitEvent(LoginEvent.EmailChanged(text))
    }

    fun onPasswordTextChanged(text: String) {
        emitEvent(LoginEvent.PasswordChanged(text))
    }

    fun login() {
        val regEmail = Regex(ConstantRegex.REGEX_EMAIL)
        val regPassword = Regex(ConstantRegex.REGEX_PASSWORD)

        if (regEmail.matches(_uiState.value.emailText) && regPassword.matches(_uiState.value.passwordText)) {
            viewModelScope.launch {
                val result =
                    loginRepository.login(_uiState.value.emailText, _uiState.value.passwordText)
                when (result) {
                    is LoadingResult.Error -> {
                        Timber.e("Error", "Error get password")
                    }
                    is LoadingResult.Success -> {
//                        userLogin.postValue(result.data)
                        loading.postValue(false)
                        sharedPreferences.edit().apply {
                            putString("ACCESS_TOKEN", result.data.accessToken)
                            putString("REFRESH_TOKEN", result.data.refreshToken)
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
            emitEvent(LoginEvent.EmailError)
            Timber.e("handleError", "Error Email")
        }

        if (!regPassword.matches(_uiState.value.passwordText)) {
            emitEvent(LoginEvent.PasswordError)
            Timber.e("handleError", "Error password")
        }
    }
}
