package com.aldera.multitasker.core.network

import com.aldera.multitasker.data.models.CategoryResponse
import com.aldera.multitasker.data.models.CreateCategoryRequest
import com.aldera.multitasker.data.models.CreateCategoryResponse
import com.aldera.multitasker.data.models.EditPasswordRequest
import com.aldera.multitasker.data.models.ExitProfileResponse
import com.aldera.multitasker.data.models.LoginRequest
import com.aldera.multitasker.data.models.LoginResponse
import com.aldera.multitasker.data.models.MultitaskerImage
import com.aldera.multitasker.data.models.ProjectResponse
import com.aldera.multitasker.data.models.PutUserRequest
import com.aldera.multitasker.data.models.RecoveryPasswordCodeRequest
import com.aldera.multitasker.data.models.RecoveryPasswordCodeResponse
import com.aldera.multitasker.data.models.RecoveryPasswordCreateRequest
import com.aldera.multitasker.data.models.RecoveryPasswordEmailRequest
import com.aldera.multitasker.data.models.RecoveryPasswordEmailResponse
import com.aldera.multitasker.data.models.RegistrationRequest
import com.aldera.multitasker.data.models.RegistrationResponse
import com.aldera.multitasker.data.models.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

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

    @GET("api/user/me")
    suspend fun getUser(): UserResponse

    @PUT("api/user/me")
    suspend fun editUser(@Body editNameRequest: PutUserRequest): UserResponse

    @POST("api/attachment/image")
    @Multipart
    suspend fun loadImage(@Part image: MultipartBody.Part?): MultitaskerImage

    @PUT("api/user/me/password")
    suspend fun editPassword(@Body editPasswordRequest: EditPasswordRequest): UserResponse

    @POST("api/logout")
    suspend fun exitProfile(): ExitProfileResponse

    @GET("api/category")
    suspend fun getCategory(): List<CategoryResponse>

    @GET("api/category/{id}/projects")
    suspend fun getProject(@Path("id") id: String): List<ProjectResponse>

    @POST("api/category")
    suspend fun createCategory(@Body createCategoryRequest: CreateCategoryRequest): CreateCategoryResponse
}
