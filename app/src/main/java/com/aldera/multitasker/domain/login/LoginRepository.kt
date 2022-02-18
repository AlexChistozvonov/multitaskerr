package com.aldera.multitasker.domain.login

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.LoginRequest
import com.aldera.multitasker.data.models.LoginResponse

interface LoginRepository {
    suspend fun login(login: String, password: String): LoadingResult<LoginResponse>
}