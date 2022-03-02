package com.aldera.multitasker.data.recovery.create

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.RecoveryPasswordCreateRequest
import com.aldera.multitasker.domain.recovery.RecoveryPasswordCreateRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RecoveryPasswordCreateRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : RecoveryPasswordCreateRepository {
    override suspend fun recoveryPasswordCreate(
        password: String,
        password2: String,
        key: String
    ) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.resetPassword(RecoveryPasswordCreateRequest(password = password, password2 = password2, key = key))
            Unit
        }
    }
}
