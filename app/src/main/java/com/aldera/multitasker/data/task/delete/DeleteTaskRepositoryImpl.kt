package com.aldera.multitasker.data.task.delete

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.task.delete.DeleteTaskRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteTaskRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : DeleteTaskRepository {
    override suspend fun deleteTask(id: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.deleteTask(id = id)
            Unit
        }
    }
}
