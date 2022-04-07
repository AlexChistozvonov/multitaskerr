package com.aldera.multitasker.domain.subtask.view

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.CreateSubtaskResponse

interface ViewSubtaskRepository {
    suspend fun getViewSubtask(id: String): LoadingResult<CreateSubtaskResponse>
}
