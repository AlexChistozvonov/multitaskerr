package com.aldera.multitasker.presentation.project.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.project.create.CreateProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val createProjectRepository: CreateProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProjectViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: CreateProjectEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onTitleTextChanged(text: String) {
        emitEvent(CreateProjectEvent.TitleChanged(text))
    }

    fun createProject(categoryId: String) {
        emitEvent(CreateProjectEvent.Loading)
        viewModelScope.launch {
            val result = createProjectRepository.createProject(
                _uiState.value.titleText,
                categoryId = categoryId
            )
            when (result) {
                is LoadingResult.Error -> emitEvent(CreateProjectEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(CreateProjectEvent.Success)
            }
        }
    }
}
