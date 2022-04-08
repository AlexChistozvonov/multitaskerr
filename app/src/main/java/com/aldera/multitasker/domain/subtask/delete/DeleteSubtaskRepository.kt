package com.aldera.multitasker.domain.subtask.delete

import com.aldera.multitasker.core.LoadingResult

interface DeleteSubtaskRepository {
    suspend fun deleteSubtask(id: String): LoadingResult<Unit>
}
