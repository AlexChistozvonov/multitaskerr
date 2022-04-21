package com.aldera.multitasker.domain.user.list

import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.UserListResponse

interface UserListRepository {
    suspend fun getUserList(): LoadingResult<List<UserListResponse>>
}
