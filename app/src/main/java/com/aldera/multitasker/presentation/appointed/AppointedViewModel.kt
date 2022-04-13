package com.aldera.multitasker.presentation.appointed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.appointed.TaskCountRepository
import com.aldera.multitasker.domain.appointed.UserSubtaskRepository
import com.aldera.multitasker.domain.appointed.UserTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AppointedViewModel @Inject constructor(
    private val userTaskRepository: UserTaskRepository,
    private val userSubtaskRepository: UserSubtaskRepository,
    private val taskCountRepository: TaskCountRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppointedViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: AppointedEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun getUserTask() {
        emitEvent(AppointedEvent.Loading)
        viewModelScope.launch {
            when (val result = userTaskRepository.getUserTask()) {
                is LoadingResult.Error -> emitEvent(AppointedEvent.Error(result.exception))
                is LoadingResult.Success -> {
                    emitEvent(AppointedEvent.Success(result.data))
                    getUserSubtask()
                }
            }
        }
    }

    private fun getUserSubtask() {
        emitEvent(AppointedEvent.Loading)
        viewModelScope.launch {
            when (val result = userSubtaskRepository.getUserSubtask()) {
                is LoadingResult.Error -> emitEvent(AppointedEvent.Error(result.exception))
                is LoadingResult.Success -> {
                    emitEvent(AppointedEvent.GetUserSubtask(result.data))
                    getTaskCount()
                }
            }
        }
    }

    private fun getTaskCount() {
        emitEvent(AppointedEvent.Loading)
        viewModelScope.launch {
            when (val result = taskCountRepository.getTaskCount()) {
                is LoadingResult.Error -> emitEvent(AppointedEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(AppointedEvent.GetTaskCount(result.data))
            }
        }
    }
}
