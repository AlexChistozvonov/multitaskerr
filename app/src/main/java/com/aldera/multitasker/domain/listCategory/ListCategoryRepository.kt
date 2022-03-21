package com.aldera.multitasker.domain.listCategory

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.CategoryResponse

interface ListCategoryRepository {
    suspend fun getCategory(): LoadingResult<List<CategoryResponse>>
}
