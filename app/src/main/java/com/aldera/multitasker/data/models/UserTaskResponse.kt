package com.aldera.multitasker.data.models

data class UserTaskResponse(
    var isCompleted: Boolean?,
    var id: String?,
    var title: String?,
    var performer: UserResponse?,
    var color: String?,
    var importance: Int?
)
