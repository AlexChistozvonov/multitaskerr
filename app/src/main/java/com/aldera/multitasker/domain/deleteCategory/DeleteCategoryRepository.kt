package com.aldera.multitasker.domain.deleteCategory

import com.aldera.multitasker.core.LoadingResult

interface DeleteCategoryRepository {
    suspend fun deleteRepository(id: String): LoadingResult<Unit>
}
