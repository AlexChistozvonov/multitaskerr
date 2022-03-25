package com.aldera.multitasker.domain.category.delete

import com.aldera.multitasker.core.LoadingResult

interface DeleteCategoryRepository {
    suspend fun deleteCategory(id: String): LoadingResult<Unit>
}
