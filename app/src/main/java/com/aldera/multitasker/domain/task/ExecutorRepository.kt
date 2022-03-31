package com.aldera.multitasker.domain.task

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.ExecutorResponse

interface ExecutorRepository {
    suspend fun getExecutor(): LoadingResult<ExecutorResponse>
}
