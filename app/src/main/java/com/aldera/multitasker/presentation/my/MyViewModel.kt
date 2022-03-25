package com.aldera.multitasker.presentation.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.category.list.ListCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MyViewModel @Inject constructor(private val listCategoryRepository: ListCategoryRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(MyViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: MyEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun getCategory() {
        emitEvent(MyEvent.Loading)
        viewModelScope.launch {
            when (val result = listCategoryRepository.getCategory()) {
                is LoadingResult.Error -> emitEvent(MyEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(MyEvent.Success(result.data))
            }
        }
    }
}
