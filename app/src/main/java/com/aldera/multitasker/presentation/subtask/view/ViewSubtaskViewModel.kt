package com.aldera.multitasker.presentation.subtask.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.subtask.delete.DeleteSubtaskRepository
import com.aldera.multitasker.domain.subtask.view.ViewSubtaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ViewSubtaskViewModel @Inject constructor(
    private val viewSubtaskRepository: ViewSubtaskRepository,
    private val deleteSubtaskRepository: DeleteSubtaskRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ViewSubtaskViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: ViewSubtaskEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun getViewSubtask(id: String) {
        emitEvent(ViewSubtaskEvent.Loading)
        viewModelScope.launch {
            when (val result = viewSubtaskRepository.getViewSubtask(id = id)) {
                is LoadingResult.Error -> emitEvent(ViewSubtaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(ViewSubtaskEvent.Success(result.data))
            }
        }
    }

    fun deleteSubtask(id: String) {
        emitEvent(ViewSubtaskEvent.Loading)
        viewModelScope.launch {
            when (val result = deleteSubtaskRepository.deleteSubtask(id)) {
                is LoadingResult.Error -> emitEvent(ViewSubtaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(ViewSubtaskEvent.DeleteSubtask)
            }
        }
    }
}
