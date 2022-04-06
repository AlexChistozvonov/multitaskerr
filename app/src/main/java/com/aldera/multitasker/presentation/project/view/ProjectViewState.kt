package com.aldera.multitasker.presentation.project.view

import com.aldera.multitasker.data.models.ProjectResponse

sealed class ProjectEvent {
    object Loading : ProjectEvent()
    data class Success(val project: List<ProjectResponse>?) : ProjectEvent()
    data class Error(val error: Exception?) : ProjectEvent()
}

data class ProjectViewState(
    val error: Exception? = null,
    val loading: Boolean = false,
    val project: List<ProjectResponse>? = null,
    val event: ProjectEvent = ProjectEvent.Loading
) {
    fun applyEvent(event: ProjectEvent) = when (event) {
        is ProjectEvent.Error -> copy(error = event.error, loading = false, event = event)
        ProjectEvent.Loading -> copy(loading = true, event = event)
        is ProjectEvent.Success -> copy(project = event.project, loading = false, event = event)
    }
}
