package com.aldera.multitasker.domain.exitProfile

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.ExitProfileResponse

interface ExitProfileRepository {
    suspend fun exitProfile(): LoadingResult<ExitProfileResponse>
}
