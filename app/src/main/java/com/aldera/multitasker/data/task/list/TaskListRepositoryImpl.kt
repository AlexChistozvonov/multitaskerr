package com.aldera.multitasker.data.task.list

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.task.list.TaskListRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TaskListRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : TaskListRepository {
    override suspend fun getTask(id: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.getTask(id = id)
        }
    }
}
