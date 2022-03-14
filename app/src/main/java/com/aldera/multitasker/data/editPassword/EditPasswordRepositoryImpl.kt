package com.aldera.multitasker.data.editPassword

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.EditPasswordRequest
import com.aldera.multitasker.domain.editPassword.EditPasswordRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class EditPasswordRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : EditPasswordRepository {
    override suspend fun editPassword(
        oldPassword: String,
        newPassword: String
    ) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.editPassword(
                EditPasswordRequest(
                    oldPassword = oldPassword,
                    newPassword = newPassword
                )
            )
        }
    }
}
