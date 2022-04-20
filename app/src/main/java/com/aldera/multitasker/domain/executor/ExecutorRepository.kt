package com.aldera.multitasker.domain.executor

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.CreateSubtaskResponse
import com.aldera.multitasker.data.models.CreateTaskResponse

interface ExecutorRepository {
    suspend fun editTaskExecutor(id: String, email: String): LoadingResult<CreateTaskResponse>
    suspend fun editSubtaskExecutor(id: String, email: String): LoadingResult<CreateSubtaskResponse>
}
