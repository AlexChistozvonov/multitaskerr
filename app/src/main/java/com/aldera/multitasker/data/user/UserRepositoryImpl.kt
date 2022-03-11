package com.aldera.multitasker.data.user

import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.models.UpdateUserData
import com.aldera.multitasker.data.models.UpdateUserRequest
import com.aldera.multitasker.domain.user.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper
) : UserRepository {
    override suspend fun getUser() = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            networkService.getUser()
        }
    }

    override suspend fun updateUser(data: UpdateUserRequest) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            when (data.payload) {
                is UpdateUserData.UpdateUser -> networkService.editUser(data.payload.user)
                is UpdateUserData.UpdatePassword -> networkService.getUser()
                is UpdateUserData.UpdateImage -> networkService.editUser(data.payload.user)
                is UpdateUserData.DeleteImage -> networkService.editUser(data.payload.user)
            }
        }
    }
}
