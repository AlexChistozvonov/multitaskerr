package com.aldera.multitasker.domain.recovery

import com.aldera.multitasker.core.LoadingResult

interface RecoveryPasswordCreateRepository {
    suspend fun recoveryPasswordCreate(
        password: String,
        password2: String,
        key: String
    ): LoadingResult<Unit>
}
