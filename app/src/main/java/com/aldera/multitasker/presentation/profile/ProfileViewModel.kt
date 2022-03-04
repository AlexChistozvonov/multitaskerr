package com.aldera.multitasker.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
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
}
