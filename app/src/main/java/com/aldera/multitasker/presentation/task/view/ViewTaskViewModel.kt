package com.aldera.multitasker.presentation.task.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.task.view.ViewTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ViewTaskViewModel @Inject constructor(
    private val viewTaskRepository: ViewTaskRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ViewTaskViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: ViewTaskViewEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun getViewTask(id: String) {
        emitEvent(ViewTaskViewEvent.Loading)
        viewModelScope.launch {
            when (val result = viewTaskRepository.getViewTask(id = id)) {
                is LoadingResult.Error -> emitEvent(ViewTaskViewEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(ViewTaskViewEvent.Success(result.data))
            }
        }
    }

    fun getSubTask(id: String) {
        emitEvent(ViewTaskViewEvent.Loading)
        viewModelScope.launch {
            when (val result = viewTaskRepository.getSubTask(id = id)) {
                is LoadingResult.Error -> emitEvent(ViewTaskViewEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(ViewTaskViewEvent.UpdateSubTask(result.data))
            }
        }
    }
}
