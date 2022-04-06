package com.aldera.multitasker.domain.task

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.UserTaskResponse

interface ExecutorRepository {
    suspend fun getExecutor(): LoadingResult<List<UserTaskResponse>>
}
