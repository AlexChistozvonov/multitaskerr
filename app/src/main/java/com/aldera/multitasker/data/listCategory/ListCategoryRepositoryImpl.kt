package com.aldera.multitasker.data.listCategory

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.listCategory.ListCategoryRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ListCategoryRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : ListCategoryRepository {
    override suspend fun getCategory() = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.getCategory()
        }
    }
}
