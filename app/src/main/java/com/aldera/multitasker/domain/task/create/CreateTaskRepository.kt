package com.aldera.multitasker.domain.task.create

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.CreateTaskRequest
import com.aldera.multitasker.data.models.CreateTaskResponse

interface CreateTaskRepository {
    suspend fun createTask(
        title: String,
        description: String,
        deadline: String,
        importance: Int,
        projectId: String,
        performerId: String
    ): LoadingResult<CreateTaskResponse>

    suspend fun editTask(
        id: String,
        data: CreateTaskRequest
    ): LoadingResult<Unit>
}
