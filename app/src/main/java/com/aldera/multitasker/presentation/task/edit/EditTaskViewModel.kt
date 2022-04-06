package com.aldera.multitasker.presentation.task.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.task.create.CreateTaskRepository
import com.aldera.multitasker.domain.task.delete.DeleteTaskRepository
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
class EditTaskViewModel @Inject constructor(
    private val editTaskRepository: CreateTaskRepository,
    private val deleteTaskRepository: DeleteTaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTaskViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: EditTaskEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onTitleTextChanged(text: String) {
        emitEvent(EditTaskEvent.TitleChanged(text))
    }

    fun onDescriptionTextChanged(text: String) {
        emitEvent(EditTaskEvent.DescriptionChanged(text))
    }

    fun onDeadlineTextChanged(text: String) {
        emitEvent(EditTaskEvent.DeadlineChanged(text))
    }

    fun onImportanceChanged(importance: Int) {
        emitEvent(EditTaskEvent.Importance(importance))
    }

    fun editTask(id: String) {
        emitEvent(EditTaskEvent.Loading)
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        val date = formatter.parse(_uiState.value.deadlineText)
        val zonedDateTime = ZonedDateTime.of(
            LocalDate.from(date).atStartOfDay(),
            ZoneId.systemDefault()
        )
        viewModelScope.launch {
            val result = editTaskRepository.editTask(
                id = id,
                title = _uiState.value.titleText,
                description = _uiState.value.descriptionText,
                deadline = zonedDateTime.format(DateTimeFormatter.ISO_INSTANT),
                importance = _uiState.value.importance,
            )
            when (result) {
                is LoadingResult.Error -> emitEvent(EditTaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(EditTaskEvent.Success)
            }
        }
    }

    fun deleteProject(id: String) {
        emitEvent(EditTaskEvent.Loading)
        viewModelScope.launch {
            when (val result = deleteTaskRepository.deleteTask(id)) {
                is LoadingResult.Error -> emitEvent(EditTaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(EditTaskEvent.Success)
            }
        }
    }
}
