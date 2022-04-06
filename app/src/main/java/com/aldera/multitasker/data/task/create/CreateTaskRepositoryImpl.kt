package com.aldera.multitasker.data.task.create

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.CreateTaskRequest
import com.aldera.multitasker.domain.task.create.CreateTaskRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateTaskRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : CreateTaskRepository {
    override suspend fun createTask(
        title: String,
        description: String,
        deadline: String,
        importance: Int,
        projectId: String,
        performerId: String
    ) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.createTask(
                CreateTaskRequest(
                    title = title,
                    description = description,
                    deadline = deadline,
                    importance = importance,
                    projectId = projectId,
                    performerId = performerId
                )
            )
        }
    }
}
