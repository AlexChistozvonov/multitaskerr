package com.aldera.multitasker.domain.subtask.create

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.CreateSubtaskResponse

interface CreateSubtaskRepository {
    suspend fun createSubtask(
        title: String,
        description: String,
        deadline: String,
        importance: Int,
        taskId: String,
        performerId: String
    ): LoadingResult<CreateSubtaskResponse>

    suspend fun editSubtask(
        id: String,
        title: String,
        description: String,
        deadline: String,
        importance: Int
    ): LoadingResult<Unit>
}
