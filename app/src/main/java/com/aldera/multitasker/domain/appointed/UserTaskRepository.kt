package com.aldera.multitasker.domain.appointed

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.UserTaskResponse

interface UserTaskRepository {
    suspend fun getUserTask(): LoadingResult<List<UserTaskResponse>>
}
