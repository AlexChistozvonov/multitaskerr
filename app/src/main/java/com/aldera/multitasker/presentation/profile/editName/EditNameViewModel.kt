package com.aldera.multitasker.presentation.profile.editName

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
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditNameViewModel @Inject constructor(
    private val userRepository: UserRepository,
    navState: SavedStateHandle
) : ViewModel() {

    private val userData = navState.get<UserResponse>(USER_DATA)

    private val _uiState = MutableStateFlow(EditNameViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: EditNameEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onEmailTextChanged(text: String) {
        emitEvent(EditNameEvent.NameChanged(text))
    }

    fun editName() = viewModelScope.launch {
        emitEvent(EditNameEvent.Loading)
        userData?.let {
            val regName = Regex(ConstantRegex.REGEX_NAME)
            if (regName.matches(_uiState.value.nameText!!)) {
                val userRequest = UpdateUserRequest(
                    payload = UpdateUserData.UpdateUser(
                        it.toPutUserRequest().copy(name = _uiState.value.nameText)
                    )
                )
                when (val result = userRepository.updateUser(userRequest)) {
                    is LoadingResult.Error -> emitEvent(EditNameEvent.Error(result.exception))
                    is LoadingResult.Success -> {
                        emitEvent(EditNameEvent.Success(result.data))
                    }
                }
            } else {
                handleError()
            }
        }
    }

    private fun handleError() {
        val regName = Regex(ConstantRegex.REGEX_NAME)
        if (!regName.matches(_uiState.value.nameText.toString())) {
            emitEvent(EditNameEvent.NameError)
        }
    }

    companion object {
        private const val USER_DATA = "userData"
    }
}
