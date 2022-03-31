package com.aldera.multitasker.data.models

data class ExecutorResponse(
    var isCompleted: Boolean?,
    var id: String,
    var title: String,
    var performer: UserResponse?
)
