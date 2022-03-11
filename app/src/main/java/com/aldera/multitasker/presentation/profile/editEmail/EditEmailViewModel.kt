package com.aldera.multitasker.presentation.profile.editEmail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.UpdateUserData
import com.aldera.multitasker.data.models.UpdateUserRequest
import com.aldera.multitasker.data.models.UserResponse
import com.aldera.multitasker.data.models.toPutUserRequest
import com.aldera.multitasker.domain.user.UserRepository
import com.aldera.multitasker.ui.util.ConstantRegex
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody

@HiltViewModel
class EditEmailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    navState: SavedStateHandle
) : ViewModel() {
    private val userData = navState.get<UserResponse>(USER_DATA)

    private val _uiState = MutableStateFlow(EditEmailViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: EditEmailEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onEmailTextChanged(text: String) {
        emitEvent(EditEmailEvent.EmailChanged(text))
    }

    fun editEmail() = viewModelScope.launch {
        emitEvent(EditEmailEvent.Loading)
        userData?.let {
            val regEmail = Regex(ConstantRegex.REGEX_EMAIL)
            if (regEmail.matches(_uiState.value.emailText!!)) {
                val userRequest = UpdateUserRequest(
                    payload = UpdateUserData.UpdateUser(
                        it.toPutUserRequest().copy(email = _uiState.value.emailText)
                    )
                )
                when (val result = userRepository.updateUser(userRequest)) {
                    is LoadingResult.Error -> emitEvent(EditEmailEvent.Error(result.exception))
                    is LoadingResult.Success -> {
                        emitEvent(EditEmailEvent.Success(result.data))
                    }
                }
            } else {
                handleError()
            }
        }
    }

    private fun handleError() {
        val regEmail = Regex(ConstantRegex.REGEX_EMAIL)
        if (!regEmail.matches(_uiState.value.emailText.toString())) {
            emitEvent(EditEmailEvent.EmailError)
            val file = File("")
            file.asRequestBody("image/*".toMediaType())
        }
    }

    companion object {
        private const val USER_DATA = "userData"
    }
}
