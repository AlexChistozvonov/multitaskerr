package com.aldera.multitasker.domain.project.create

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.ProjectResponse

interface CreateProjectRepository {
    suspend fun createProject(
        title: String?,
        categoryId: String?
    ): LoadingResult<ProjectResponse>

    suspend fun editProject(
        id: String,
        title: String?,
        categoryId: String?
    ): LoadingResult<ProjectResponse>
}
