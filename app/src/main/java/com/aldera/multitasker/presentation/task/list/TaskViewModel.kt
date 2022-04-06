package com.aldera.multitasker.presentation.task.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.task.list.TaskListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskListRepository: TaskListRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: TaskEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun getTask(id: String) {
        emitEvent(TaskEvent.Loading)
        viewModelScope.launch {
            when (val result = taskListRepository.getTask(id = id)) {
                is LoadingResult.Error -> emitEvent(TaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(TaskEvent.Success(result.data))
            }
        }
    }
}
