package com.aldera.multitasker.presentation.subtask.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.subtask.create.CreateSubtaskRepository
import com.aldera.multitasker.domain.subtask.delete.DeleteSubtaskRepository
import com.aldera.multitasker.ui.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@HiltViewModel
class EditSubtaskViewModel @Inject constructor(
    private val editSubtaskRepository: CreateSubtaskRepository,
    private val deleteSubtaskRepository: DeleteSubtaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditSubtaskViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: EditSubtaskEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onTitleTextChanged(text: String) {
        emitEvent(EditSubtaskEvent.TitleChanged(text))
    }

    fun onDescriptionTextChanged(text: String) {
        emitEvent(EditSubtaskEvent.DescriptionChanged(text))
    }

    fun onDeadlineTextChanged(text: String) {
        emitEvent(EditSubtaskEvent.DeadlineChanged(text))
    }

    fun onImportanceChanged(importance: Int) {
        emitEvent(EditSubtaskEvent.Importance(importance))
    }

    fun editSubtask(id: String) {
        emitEvent(EditSubtaskEvent.Loading)
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        val date = formatter.parse(_uiState.value.deadlineText)
        val zonedDateTime = ZonedDateTime.of(
            LocalDate.from(date).atStartOfDay(),
            ZoneId.systemDefault()
        )
        viewModelScope.launch {
            val result = editSubtaskRepository.editSubtask(
                id = id,
                title = _uiState.value.titleText,
                description = _uiState.value.descriptionText,
                deadline = zonedDateTime.format(DateTimeFormatter.ISO_INSTANT),
                importance = _uiState.value.importance,
            )
            when (result) {
                is LoadingResult.Error -> emitEvent(EditSubtaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(EditSubtaskEvent.Success)
            }
        }
    }

    fun deleteTask(id: String) {
        emitEvent(EditSubtaskEvent.Loading)
        viewModelScope.launch {
            when (val result = deleteSubtaskRepository.deleteSubtask(id)) {
                is LoadingResult.Error -> emitEvent(EditSubtaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(EditSubtaskEvent.Success)
            }
        }
    }
}
