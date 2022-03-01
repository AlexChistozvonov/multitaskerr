package com.aldera.multitasker.data.recovery.code

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.RecoveryPasswordCodeRequest
import com.aldera.multitasker.domain.recovery.RecoveryPasswordCodeRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RecoveryPasswordCodeRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : RecoveryPasswordCodeRepository {
    override suspend fun recoveryPasswordCode(code: String, key: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.confirmCode(RecoveryPasswordCodeRequest(code = code, key = key))
        }
    }
}
