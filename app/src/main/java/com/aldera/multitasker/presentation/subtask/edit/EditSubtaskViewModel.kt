package com.aldera.multitasker.presentation.subtask.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.CreateSubtaskResponse
import com.aldera.multitasker.data.models.toCreateSubtaskRequest
import com.aldera.multitasker.domain.executor.ExecutorRepository
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
class EditSubtaskViewModel @Inject constructor(
    private val editSubtaskRepository: CreateSubtaskRepository,
    private val executorRepository: UserListRepository,
    private val editExecutorRepository: ExecutorRepository,
    navState: SavedStateHandle
) : ViewModel() {

    private val subTaskData = navState.get<CreateSubtaskResponse>(SUBTASK_DATA)

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

    fun onPerformerIdChanged(text: String) {
        emitEvent(EditSubtaskEvent.ExecutorEmail(text))
    }

    fun getExecutor() {
        emitEvent(EditSubtaskEvent.Loading)
        viewModelScope.launch {
            when (val result = executorRepository.getUserList()) {
                is LoadingResult.Error -> emitEvent(EditSubtaskEvent.Error(result.exception))
                is LoadingResult.Success -> {
                    emitEvent(EditSubtaskEvent.GetExecutor(result.data))
                }
            }
        }
    }

    fun editSubtask(id: String) = viewModelScope.launch {
        if (!_uiState.value.executorEmail.isNullOrEmpty()) editSubtaskExecutor(id = id)
        emitEvent(EditSubtaskEvent.Loading)
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        val date = formatter.parse(_uiState.value.deadlineText)
        val dateTime = LocalDate.from(date).atStartOfDay()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        subTaskData?.let {
            val createSubtaskRequest = it.toCreateSubtaskRequest()?.copy(
                title = _uiState.value.titleText ?: subTaskData.title,
                description = _uiState.value.descriptionText ?: subTaskData.description,
                deadline = dateTime,
                importance = _uiState.value.importance,
            )

            val result = createSubtaskRequest?.let { createSubtask ->
                editSubtaskRepository.editSubtask(
                    id = id,
                    createSubtask
                )
            }
            when (result) {
                is LoadingResult.Error -> emitEvent(EditSubtaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(EditSubtaskEvent.Success)
            }
        }
    }

    private fun editSubtaskExecutor(id: String) {
        emitEvent(EditSubtaskEvent.Loading)
        viewModelScope.launch {
            val result = _uiState.value.executorEmail?.let {
                editExecutorRepository.editSubtaskExecutor(
                    id = id,
                    email = it
                )
            }
            when (result) {
                is LoadingResult.Error -> emitEvent(EditSubtaskEvent.Error(result.exception))
                is LoadingResult.Success -> emitEvent(EditSubtaskEvent.EditExecutor(result.data))
            }
        }
    }

    companion object {
        private const val SUBTASK_DATA = "subTask"
    }
}
