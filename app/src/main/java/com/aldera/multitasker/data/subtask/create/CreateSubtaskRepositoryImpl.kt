package com.aldera.multitasker.data.subtask.create

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.CreateSubtaskRequest
import com.aldera.multitasker.domain.subtask.create.CreateSubtaskRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateSubtaskRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : CreateSubtaskRepository {
    override suspend fun createSubtask(
        title: String,
        description: String,
        deadline: String,
        importance: Int,
        taskId: String,
        performerId: String
    ) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.createSubtask(
                CreateSubtaskRequest(
                    title = title,
                    description = description,
                    deadline = deadline,
                    importance = importance,
                    taskId = taskId,
                    performerId = performerId
                )
            )
        }
    }

    override suspend fun editSubtask(id: String, data: CreateSubtaskRequest) =
        withContext(coroutineDispatcher) {
            runLoading(errorMapper) {
                networkService.editSubtask(
                    id = id,
                    data
                )
                Unit
            }
        }
}
