package com.aldera.multitasker.data.task.view

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.task.view.ViewTaskRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ViewTaskRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : ViewTaskRepository {
    override suspend fun getViewTask(id: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.getViewTask(id = id)
        }
    }

    override suspend fun getSubTask(id: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.getSubTasks(id = id)
        }
    }
}
