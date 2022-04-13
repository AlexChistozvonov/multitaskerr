package com.aldera.multitasker.domain.appointed

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.UserTaskResponse

interface UserSubtaskRepository {
    suspend fun getUserSubtask(): LoadingResult<List<UserTaskResponse>>
}
