package com.aldera.multitasker.data.recovery.email

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.RecoveryPasswordEmailRequest
import com.aldera.multitasker.domain.recovery.RecoveryPasswordEmailRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RecoveryPasswordEmailRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : RecoveryPasswordEmailRepository {
    override suspend fun recoveryPasswordEmail(login: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.sendCode(RecoveryPasswordEmailRequest(email = login))
        }
    }
}
