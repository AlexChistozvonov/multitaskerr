package com.aldera.multitasker.data.category.create

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.CreateCategoryRequest
import com.aldera.multitasker.domain.category.create.CreateCategoryRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateCategoryRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : CreateCategoryRepository {
    override suspend fun createCategory(
        title: String,
        color: String
    ) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.createCategory(CreateCategoryRequest(title = title, color = color))
        }
    }

    override suspend fun editCategory(
        id: String,
        title: String,
        color: String
    ) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.editCategory(
                id = id,
                CreateCategoryRequest(title = title, color = color)
            )
        }
    }
}
