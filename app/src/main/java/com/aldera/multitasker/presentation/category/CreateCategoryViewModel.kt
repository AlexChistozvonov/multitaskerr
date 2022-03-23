package com.aldera.multitasker.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.createCategory.CreateCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateCategoryViewModel @Inject constructor(
    private val createCategoryRepository: CreateCategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateCategoryViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: CreateCategoryEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onTitleTextChanged(text: String) {
        emitEvent(CreateCategoryEvent.TitleChanged(text))
    }

    fun onSelectedColorChanged(colorItem: ColorItem) {
        emitEvent(CreateCategoryEvent.ColorChanged(colorItem))
    }

    fun createCategory() {
        emitEvent(CreateCategoryEvent.Loading)
        viewModelScope.launch {
            val result = createCategoryRepository.createCategory(
                _uiState.value.titleText,
                _uiState.value.colorText
            )
            when (result) {
                is LoadingResult.Error -> emitEvent(CreateCategoryEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(CreateCategoryEvent.Success)
            }
        }
    }
}
