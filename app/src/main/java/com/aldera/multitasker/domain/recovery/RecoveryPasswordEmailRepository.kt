package com.aldera.multitasker.domain.recovery

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.RecoveryPasswordEmailResponse

interface RecoveryPasswordEmailRepository {
    suspend fun recoveryPasswordEmail(login: String): LoadingResult<RecoveryPasswordEmailResponse>
}
