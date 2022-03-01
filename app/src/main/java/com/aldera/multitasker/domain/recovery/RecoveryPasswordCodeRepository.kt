package com.aldera.multitasker.domain.recovery

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.RecoveryPasswordCodeResponse

interface RecoveryPasswordCodeRepository {
    suspend fun recoveryPasswordCode(code: String, key: String): LoadingResult<RecoveryPasswordCodeResponse>
}
