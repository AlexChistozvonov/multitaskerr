package com.aldera.multitasker.data.task

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.task.ExecutorRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ExecutorRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : ExecutorRepository {
    override suspend fun getExecutor() = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.getExecutor()
        }
    }
}
