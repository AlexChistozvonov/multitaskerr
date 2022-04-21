package com.aldera.multitasker.data.executor

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.EditExecutorRequest
import com.aldera.multitasker.domain.executor.ExecutorRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ExecutorRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : ExecutorRepository {
    override suspend fun editTaskExecutor(id: String, email: String) =
        withContext(coroutineDispatcher) {
            runLoading(errorMapper) {
                networkService.editTaskExecutor(id = id, EditExecutorRequest(email = email))
            }
        }

    override suspend fun editSubtaskExecutor(
        id: String,
        email: String
    ) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.editSubtaskExecutor(id = id, EditExecutorRequest(email = email))
        }
    }
}
