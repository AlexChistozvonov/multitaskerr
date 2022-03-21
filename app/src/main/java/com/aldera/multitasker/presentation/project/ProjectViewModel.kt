package com.aldera.multitasker.presentation.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.projectList.ProjectListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectListRepository: ProjectListRepository,
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ProjectViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: ProjectEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun getProject(id: String) {
        emitEvent(ProjectEvent.Loading)
        viewModelScope.launch {
            when (val result = projectListRepository.getProject(id = id)) {
                is LoadingResult.Error -> emitEvent(ProjectEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(ProjectEvent.Success(result.data))
            }
        }
    }
}
