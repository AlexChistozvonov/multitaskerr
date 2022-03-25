package com.aldera.multitasker.domain.project.create

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.CreateProjectResponse

interface CreateProjectRepository {
    suspend fun createProject(
        title: String?,
        categoryId: String?
    ): LoadingResult<CreateProjectResponse>
}
