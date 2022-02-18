package com.aldera.multitasker.data.login

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.LoginRequest
import com.aldera.multitasker.domain.login.LoginRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LoginRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : LoginRepository {
    override suspend fun login(login: String, password: String) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.login(LoginRequest(email = login, password = password))
        }
    }
}
