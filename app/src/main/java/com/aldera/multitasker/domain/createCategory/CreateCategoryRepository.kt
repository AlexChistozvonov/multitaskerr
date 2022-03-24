package com.aldera.multitasker.domain.createCategory

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.CreateCategoryResponse

interface CreateCategoryRepository {
    suspend fun createCategory(title: String, color: String): LoadingResult<CreateCategoryResponse>
    suspend fun editCategory(
        id: String,
        title: String,
        color: String
    ): LoadingResult<CreateCategoryResponse>
}
