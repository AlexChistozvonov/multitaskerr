package com.aldera.multitasker.data.exitProfile

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.domain.exitProfile.ExitProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ExitProfileRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : ExitProfileRepository {
    override suspend fun exitProfile() = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.exitProfile()
        }
    }
}
