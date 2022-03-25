package com.aldera.multitasker.domain.project.delete

import com.aldera.multitasker.core.LoadingResult

interface DeleteProjectRepository {
    suspend fun deleteProject(id: String): LoadingResult<Unit>
}
