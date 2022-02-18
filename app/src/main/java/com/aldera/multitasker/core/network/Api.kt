package com.aldera.multitasker.core.network

import com.aldera.multitasker.data.models.LoginRequest
import com.aldera.multitasker.data.models.LoginResponse
import retrofit2.http.*

interface Api {
    @POST("/api/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}