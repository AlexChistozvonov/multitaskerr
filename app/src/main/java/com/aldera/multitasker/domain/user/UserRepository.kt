package com.aldera.multitasker.domain.user

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.UserResponse

interface UserRepository {
    suspend fun getUser(): LoadingResult<UserResponse>
}
