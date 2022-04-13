package com.aldera.multitasker.data.appointed

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.appointed.UserSubtaskRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserSubtaskRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : UserSubtaskRepository {
    override suspend fun getUserSubtask() = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.getUserSubtask()
        }
    }
}
