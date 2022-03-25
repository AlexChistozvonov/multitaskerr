package com.aldera.multitasker.data.project.create

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.CreateProjectRequest
import com.aldera.multitasker.domain.project.create.CreateProjectRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateProjectRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : CreateProjectRepository {
    override suspend fun createProject(
        title: String?,
        categoryId: String?
    ) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.createProject(
                CreateProjectRequest(
                    title = title,
                    categoryId = categoryId
                )
            )
        }
    }

    override suspend fun editProject(
        id: String,
        title: String?,
        categoryId: String?
    ) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.editProject(
                id = id,
                CreateProjectRequest(
                    title = title,
                    categoryId = categoryId
                )
            )
        }
    }
}
