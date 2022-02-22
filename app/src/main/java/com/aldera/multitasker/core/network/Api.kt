package com.aldera.multitasker.core.network

import com.aldera.multitasker.data.models.LoginRequest
import com.aldera.multitasker.data.models.LoginResponse
import com.aldera.multitasker.data.models.RegistrationRequest
import com.aldera.multitasker.data.models.RegistrationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("/api/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/api/registration")
    suspend fun registration(@Body registrationRequest: RegistrationRequest): RegistrationResponse
}
