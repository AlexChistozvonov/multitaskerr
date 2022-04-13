package com.aldera.multitasker.domain.appointed

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.TaskCountResponse

interface TaskCountRepository {
    suspend fun getTaskCount(): LoadingResult<TaskCountResponse>
}
