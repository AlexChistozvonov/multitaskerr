package com.aldera.multitasker.core.network

import com.aldera.multitasker.data.models.CategoryResponse
import com.aldera.multitasker.data.models.CreateCategoryRequest
import com.aldera.multitasker.data.models.CreateCategoryResponse
import com.aldera.multitasker.data.models.CreateProjectRequest
import com.aldera.multitasker.data.models.CreateSubtaskRequest
import com.aldera.multitasker.data.models.CreateSubtaskResponse
import com.aldera.multitasker.data.models.CreateTaskRequest
import com.aldera.multitasker.data.models.CreateTaskResponse
import com.aldera.multitasker.data.models.EditExecutorRequest
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
import com.aldera.multitasker.data.models.TaskCountResponse
import com.aldera.multitasker.data.models.TaskResponse
import com.aldera.multitasker.data.models.UserListResponse
import com.aldera.multitasker.data.models.UserResponse
import com.aldera.multitasker.data.models.UserTaskResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @PUT("api/category/{id}")
    suspend fun editCategory(
        @Path("id") id: String,
        @Body editCategoryRequest: CreateCategoryRequest
    ): CreateCategoryResponse

    @DELETE("api/category/{id}")
    suspend fun deleteCategory(@Path("id") id: String): Response<Unit>

    @POST("api/project")
    suspend fun createProject(@Body createProjectRequest: CreateProjectRequest): ProjectResponse

    @PUT("api/project/{id}")
    suspend fun editProject(
        @Path("id") id: String,
        @Body editProjectRequest: CreateProjectRequest
    ): ProjectResponse

    @DELETE("api/project/{id}")
    suspend fun deleteProject(@Path("id") id: String): Response<Unit>

    @GET("api/project/{id}/tasks")
    suspend fun getTask(@Path("id") id: String): List<TaskResponse>

    @POST("api/task")
    suspend fun createTask(@Body createTaskRequest: CreateTaskRequest): CreateTaskResponse

    @GET("api/task/{id}")
    suspend fun getViewTask(@Path("id") id: String): CreateTaskResponse

    @GET("api/task/{id}/sub-tasks")
    suspend fun getSubTasks(@Path("id") id: String): List<TaskResponse>

    @PUT("api/task/{id}")
    suspend fun editTask(
        @Path("id") id: String,
        @Body editTaskRequest: CreateTaskRequest
    ): Response<Unit>

    @DELETE("api/task/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Unit>

    @POST("api/sub-task")
    suspend fun createSubtask(@Body createSubtaskRequest: CreateSubtaskRequest): CreateSubtaskResponse

    @GET("api/sub-task/{id}")
    suspend fun getViewSubtask(@Path("id") id: String): CreateSubtaskResponse

    @PUT("api/sub-task/{id}")
    suspend fun editSubtask(
        @Path("id") id: String,
        @Body editSubtaskRequest: CreateSubtaskRequest
    ): Response<Unit>

    @DELETE("api/sub-task/{id}")
    suspend fun deleteSubtask(@Path("id") id: String): Response<Unit>

    @GET("api/user/me/tasks")
    suspend fun getUserTask(): List<UserTaskResponse>

    @GET("api/user/me/sub-tasks")
    suspend fun getUserSubtask(): List<UserTaskResponse>

    @GET("api/user/me/count/task")
    suspend fun getTaskCount(): TaskCountResponse

    @GET("api/user")
    suspend fun getUserList(): List<UserListResponse>

    @PUT("api/task/{id}/performer")
    suspend fun editTaskExecutor(
        @Path("id") id: String,
        @Body editExecutorRequest: EditExecutorRequest
    ): CreateTaskResponse

    @PUT("api/sub-task/{id}/performer")
    suspend fun editSubtaskExecutor(
        @Path("id") id: String,
        @Body editExecutorRequest: EditExecutorRequest
    ): CreateSubtaskResponse
}
