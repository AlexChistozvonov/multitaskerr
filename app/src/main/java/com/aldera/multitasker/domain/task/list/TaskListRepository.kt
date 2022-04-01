package com.aldera.multitasker.domain.task.list

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.TaskResponse

interface TaskListRepository {
    suspend fun getTask(id: String): LoadingResult<List<TaskResponse>>
}
