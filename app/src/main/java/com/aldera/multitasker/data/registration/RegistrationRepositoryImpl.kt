package com.aldera.multitasker.data.registration

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.RegistrationRequest
import com.aldera.multitasker.data.models.RegistrationResponse
import com.aldera.multitasker.domain.registration.RegistrationRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RegistrationRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : RegistrationRepository {
    override suspend fun registration(
        login: String,
        password: String,
        password2: String,
    ): LoadingResult<RegistrationResponse> = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.registration(RegistrationRequest(email = login, password = password, password2 = password2))
        }
    }
}
