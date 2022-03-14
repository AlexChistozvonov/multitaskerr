package com.aldera.multitasker.domain.editPassword

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.UserResponse

interface EditPasswordRepository {
    suspend fun editPassword(oldPassword: String, newPassword: String): LoadingResult<UserResponse>
}
