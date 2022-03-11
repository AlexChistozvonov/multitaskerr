package com.aldera.multitasker.domain.user

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.UpdateUserRequest
import com.aldera.multitasker.data.models.UserResponse

interface UserRepository {
    suspend fun getUser(): LoadingResult<UserResponse>
    suspend fun updateUser(data: UpdateUserRequest): LoadingResult<UserResponse>
}
