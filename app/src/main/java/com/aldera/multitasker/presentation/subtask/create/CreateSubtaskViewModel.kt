package com.aldera.multitasker.presentation.subtask.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.subtask.create.CreateSubtaskRepository
import com.aldera.multitasker.domain.user.list.UserListRepository
import com.aldera.multitasker.ui.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@HiltViewModel
class CreateSubtaskViewModel @Inject constructor(
    private val executorRepository: UserListRepository,
    private val createSubtaskRepository: CreateSubtaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateSubtaskViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: CreateSubtaskEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onTitleTextChanged(text: String) {
        emitEvent(CreateSubtaskEvent.TitleChanged(text))
    }

    fun onDescriptionTextChanged(text: String) {
        emitEvent(CreateSubtaskEvent.DescriptionChanged(text))
    }

    fun onDeadlineTextChanged(text: String) {
        emitEvent(CreateSubtaskEvent.DeadlineChanged(text))
    }

    fun onImportanceChanged(importance: Int) {
        emitEvent(CreateSubtaskEvent.Importance(importance))
    }

    private fun onPerformerIdChanged(text: String) {
        emitEvent(CreateSubtaskEvent.ExecutorEmail(text))
    }

    fun onEmailChange(text: String?) {
        val performerId = _uiState.value.userList?.firstOrNull { it.email == text }?.id
        performerId?.let {
            onPerformerIdChanged(it)
        }
    }

    fun getExecutor() {
        emitEvent(CreateSubtaskEvent.Loading)
        viewModelScope.launch {
            when (val result = executorRepository.getUserList()) {
                is LoadingResult.Error -> emitEvent(CreateSubtaskEvent.Error(result.exception))
                is LoadingResult.Success -> {
                    emitEvent(CreateSubtaskEvent.GetExecutor(result.data))
                }
            }
        }
    }

    fun createSubtask(taskId: String) {
        emitEvent(CreateSubtaskEvent.Loading)
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        val date = formatter.parse(_uiState.value.deadlineText)
        val dateTime = LocalDate.from(date).atStartOfDay()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        viewModelScope.launch {
            val result = createSubtaskRepository.createSubtask(
                title = _uiState.value.titleText,
                description = _uiState.value.descriptionText,
                deadline = dateTime.format(DateTimeFormatter.ISO_INSTANT),
                importance = _uiState.value.importance,
                taskId = taskId,
                performerId = _uiState.value.executorId
            )
            when (result) {
                is LoadingResult.Error -> emitEvent(CreateSubtaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(CreateSubtaskEvent.Success)
            }
        }
    }
}
