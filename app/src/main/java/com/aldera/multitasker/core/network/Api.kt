package com.aldera.multitasker.core.network

import com.aldera.multitasker.data.models.LoginRequest
import com.aldera.multitasker.data.models.LoginResponse
import com.aldera.multitasker.data.models.RecoveryPasswordCodeRequest
import com.aldera.multitasker.data.models.RecoveryPasswordCodeResponse
import com.aldera.multitasker.data.models.RecoveryPasswordCreateRequest
import com.aldera.multitasker.data.models.RecoveryPasswordEmailRequest
import com.aldera.multitasker.data.models.RecoveryPasswordEmailResponse
import com.aldera.multitasker.data.models.RegistrationRequest
import com.aldera.multitasker.data.models.RegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("/api/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/api/registration")
    suspend fun registration(@Body registrationRequest: RegistrationRequest): RegistrationResponse

    @POST("api/password/send")
    suspend fun sendCode(@Body recoveryPasswordEmailRequest: RecoveryPasswordEmailRequest): RecoveryPasswordEmailResponse

    @POST("api/password/confirm")
    suspend fun confirmCode(@Body recoveryPasswordCodeRequest: RecoveryPasswordCodeRequest): RecoveryPasswordCodeResponse

    @POST("api/password/reset")
    suspend fun resetPassword(@Body recoveryPasswordCreateRequest: RecoveryPasswordCreateRequest): Response<Unit>
}
