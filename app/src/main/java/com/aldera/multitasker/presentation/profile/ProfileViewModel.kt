package com.aldera.multitasker.presentation.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.MultitaskerImage
import com.aldera.multitasker.data.models.UpdateUserData
import com.aldera.multitasker.data.models.UpdateUserRequest
import com.aldera.multitasker.data.models.toPutUserRequest
import com.aldera.multitasker.domain.common.CommonRepository
import com.aldera.multitasker.domain.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val commonRepository: CommonRepository
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UserViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: UserEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun loadData() {
        emitEvent(UserEvent.Loading)
        viewModelScope.launch {
            when (val result = userRepository.getUser()) {
                is LoadingResult.Error -> emitEvent(UserEvent.Error(result.exception))
                is LoadingResult.Success -> {
                    emitEvent(UserEvent.Success(result.data))
                }
            }
        }
    }

    fun deleteAvatar() = viewModelScope.launch {
        val updatedUser = _uiState.value.user
        updatedUser?.let {
            val userRequest = UpdateUserRequest(
                payload = UpdateUserData.DeleteImage(
                    it.copy(avatar = null).toPutUserRequest()
                )
            )
            when (val result = userRepository.updateUser(userRequest)) {
                is LoadingResult.Error -> {
                    emitEvent(UserEvent.ImageError(result.exception))
                }
                is LoadingResult.Success -> {
                    emitEvent(UserEvent.Success(result.data))
                }
            }
        }
    }

    fun preloadImage(uri: Uri?) = viewModelScope.launch {
        emitEvent(UserEvent.ImageLoading)
        uri?.let {
            when (val result = commonRepository.uploadImage(it)) {
                is LoadingResult.Error -> emitEvent(UserEvent.ImageError(result.exception))
                is LoadingResult.Success -> updateAvatar(result.data)
            }
        }
    }

    private fun updateAvatar(image: MultitaskerImage) = viewModelScope.launch {
        val updatedUser = _uiState.value.user
        updatedUser?.let {
            val userRequest = UpdateUserRequest(
                payload = UpdateUserData.UpdateUser(
                    it.copy(avatar = image).toPutUserRequest()
                )
            )
            when (val result = userRepository.updateUser(userRequest)) {
                is LoadingResult.Error -> emitEvent(UserEvent.ImageError(result.exception))
                is LoadingResult.Success -> emitEvent(UserEvent.Success(result.data))
            }
        }
    }
}
