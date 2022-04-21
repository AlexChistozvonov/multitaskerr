package com.aldera.multitasker.presentation.task.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.domain.task.create.CreateTaskRepository
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
class CreateTaskViewModel @Inject constructor(
    private val executorRepository: UserListRepository,
    private val createTaskRepository: CreateTaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTaskViewState())
    val uiState = _uiState.asStateFlow()

    private fun emitEvent(event: CreateTaskEvent) {
        _uiState.value = _uiState.value.applyEvent(event)
    }

    fun onTitleTextChanged(text: String) {
        emitEvent(CreateTaskEvent.TitleChanged(text))
    }

    fun onDescriptionTextChanged(text: String) {
        emitEvent(CreateTaskEvent.DescriptionChanged(text))
    }

    fun onDeadlineTextChanged(text: String) {
        emitEvent(CreateTaskEvent.DeadlineChanged(text))
    }

    fun onImportanceChanged(importance: Int) {
        emitEvent(CreateTaskEvent.Importance(importance))
    }

    private fun onPerformerIdChanged(text: String) {
        emitEvent(CreateTaskEvent.ExecutorEmail(text))
    }

    fun onEmailChange(text: String?) {
        val performerId = _uiState.value.userList?.firstOrNull { it.email == text }?.id
        performerId?.let {
            onPerformerIdChanged(it)
        }
    }

    fun getExecutor() {
        emitEvent(CreateTaskEvent.Loading)
        viewModelScope.launch {
            when (val result = executorRepository.getUserList()) {
                is LoadingResult.Error -> emitEvent(CreateTaskEvent.Error(result.exception))
                is LoadingResult.Success -> {
                    emitEvent(CreateTaskEvent.GetExecutor(result.data))
                }
            }
        }
    }

    fun createTask(projectId: String) {
        emitEvent(CreateTaskEvent.Loading)
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        val date = formatter.parse(_uiState.value.deadlineText)
        val dateTime = LocalDate.from(date).atStartOfDay()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        viewModelScope.launch {
            val result = createTaskRepository.createTask(
                title = _uiState.value.titleText,
                description = _uiState.value.descriptionText,
                deadline = dateTime,
                importance = _uiState.value.importance,
                projectId = projectId,
                performerId = _uiState.value.executorId
            )
            when (result) {
                is LoadingResult.Error -> emitEvent(CreateTaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(CreateTaskEvent.Success(result.data))
            }
        }
    }
}
