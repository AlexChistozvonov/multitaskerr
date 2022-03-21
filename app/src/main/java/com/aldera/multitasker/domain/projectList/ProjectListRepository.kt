package com.aldera.multitasker.domain.projectList

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.ProjectResponse

interface ProjectListRepository {
    suspend fun getProject(id: String): LoadingResult<List<ProjectResponse>>
}
