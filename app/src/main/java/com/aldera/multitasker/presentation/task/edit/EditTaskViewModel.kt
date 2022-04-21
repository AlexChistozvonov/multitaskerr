package com.aldera.multitasker.presentation.task.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.CreateTaskResponse
import com.aldera.multitasker.data.models.toCreateTaskRequest
import com.aldera.multitasker.domain.executor.ExecutorRepository
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
class EditTaskViewModel @Inject constructor(
    private val executorRepository: UserListRepository,
    private val editTaskRepository: CreateTaskRepository,
    private val editExecutorRepository: ExecutorRepository,
    navState: SavedStateHandle
) : ViewModel() {

    private val taskData = navState.get<CreateTaskResponse>(TASK_DATA)

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

    fun onPerformerIdChanged(text: String) {
        emitEvent(EditTaskEvent.ExecutorEmail(text))
    }

    fun getExecutor() {
        emitEvent(EditTaskEvent.Loading)
        viewModelScope.launch {
            when (val result = executorRepository.getUserList()) {
                is LoadingResult.Error -> emitEvent(EditTaskEvent.Error(result.exception))
                is LoadingResult.Success -> {
                    emitEvent(EditTaskEvent.GetExecutor(result.data))
                }
            }
        }
    }

    fun editTask(id: String) = viewModelScope.launch {
        if (!_uiState.value.executorEmail.isNullOrEmpty()) editTaskExecutor(id = id)
        emitEvent(EditTaskEvent.Loading)
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        val date = formatter.parse(_uiState.value.deadlineText)
        val dateTime = LocalDate.from(date).atStartOfDay()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        taskData?.let {
            val createTaskRequest = it.toCreateTaskRequest()?.copy(
                title = _uiState.value.titleText ?: taskData.title,
                description = _uiState.value.descriptionText ?: taskData.description,
                deadline = dateTime,
                importance = _uiState.value.importance,
            )
            val result = createTaskRequest?.let { createTask ->
                editTaskRepository.editTask(
                    id = id,
                    createTask
                )
            }
            when (result) {
                is LoadingResult.Error -> emitEvent(EditTaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(EditTaskEvent.Success)
            }
        }
    }

    private fun editTaskExecutor(id: String) {
        emitEvent(EditTaskEvent.Loading)
        viewModelScope.launch {
            val result = _uiState.value.executorEmail?.let {
                editExecutorRepository.editTaskExecutor(
                    id = id,
                    email = it
                )
            }
            when (result) {
                is LoadingResult.Error -> emitEvent(EditTaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(EditTaskEvent.EditExecutor(result.data))
            }
        }
    }

    companion object {
        private const val TASK_DATA = "createTask"
    }
}
