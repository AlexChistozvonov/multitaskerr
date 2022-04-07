package com.aldera.multitasker.data.subtask.view

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.subtask.view.ViewSubtaskRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ViewSubtaskRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : ViewSubtaskRepository {
    override suspend fun getViewSubtask(id: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.getViewSubtask(id = id)
        }
    }
}
