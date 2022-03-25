package com.aldera.multitasker.data.project.delete

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.project.delete.DeleteProjectRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteProjectRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : DeleteProjectRepository {
    override suspend fun deleteProject(id: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.deleteProject(id = id)
            Unit
        }
    }
}
