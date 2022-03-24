package com.aldera.multitasker.data.deleteCategory

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.deleteCategory.DeleteCategoryRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteCategoryRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : DeleteCategoryRepository {
    override suspend fun deleteRepository(id: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.deleteCategory(id = id)
            Unit
        }
    }
}
