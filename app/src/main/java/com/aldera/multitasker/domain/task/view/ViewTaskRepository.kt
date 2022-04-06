package com.aldera.multitasker.domain.task.view

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.CreateTaskResponse
import com.aldera.multitasker.data.models.TaskResponse

interface ViewTaskRepository {
    suspend fun getViewTask(id: String): LoadingResult<CreateTaskResponse>
    suspend fun getSubTask(id: String): LoadingResult<List<TaskResponse>>
}
