package com.aldera.multitasker.presentation.project.edit

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
class EditProjectViewModel @Inject constructor(
    private val editProjectRepository: CreateProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProjectViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: EditProjectEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onTitleTextChanged(text: String) {
        emitEvent(EditProjectEvent.TitleChanged(text))
    }

    fun editProject(id: String, categoryId: String) {
        emitEvent(EditProjectEvent.Loading)
        viewModelScope.launch {
            val result = editProjectRepository.editProject(
                id = id,
                _uiState.value.titleText,
                categoryId = categoryId
            )
            when (result) {
                is LoadingResult.Error -> emitEvent(EditProjectEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(EditProjectEvent.Success)
            }
        }
    }
}
