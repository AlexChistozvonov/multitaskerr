package com.aldera.multitasker.data.project.list

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.project.list.ProjectListRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ProjectListRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : ProjectListRepository {
    override suspend fun getProject(id: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.getProject(id = id)
        }
    }
}
