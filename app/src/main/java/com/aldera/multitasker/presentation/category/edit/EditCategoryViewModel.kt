package com.aldera.multitasker.presentation.category.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.category.create.CreateCategoryRepository
import com.aldera.multitasker.domain.category.delete.DeleteCategoryRepository
import com.aldera.multitasker.presentation.category.ColorItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val editCategoryRepository: CreateCategoryRepository,
    private val deleteCategoryRepository: DeleteCategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditCategoryViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: EditCategoryEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onTitleTextChanged(text: String) {
        emitEvent(EditCategoryEvent.TitleChanged(text))
    }

    fun onSelectedColorChanged(colorItem: ColorItem) {
        emitEvent(EditCategoryEvent.ColorChanged(colorItem))
    }

    fun editCategory(id: String) {
        emitEvent(EditCategoryEvent.Loading)
        viewModelScope.launch {
            val result = editCategoryRepository.editCategory(
                id = id,
                _uiState.value.titleText,
                _uiState.value.colorText
            )
            when (result) {
                is LoadingResult.Error -> emitEvent(EditCategoryEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(EditCategoryEvent.Success)
            }
        }
    }

    fun deleteCategory(id: String) {
        emitEvent(EditCategoryEvent.Loading)
        viewModelScope.launch {
            when (val result = deleteCategoryRepository.deleteRepository(id)) {
                is LoadingResult.Error -> emitEvent(EditCategoryEvent.Error(result.exception))
                is LoadingResult.Success -> {
                    emitEvent(EditCategoryEvent.ExitLoading)
                }
            }
        }
    }
}
