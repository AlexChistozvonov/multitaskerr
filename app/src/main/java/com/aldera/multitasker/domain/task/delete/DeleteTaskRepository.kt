package com.aldera.multitasker.domain.task.delete

import com.aldera.multitasker.core.LoadingResult

interface DeleteTaskRepository {
    suspend fun deleteTask(id: String): LoadingResult<Unit>
}
