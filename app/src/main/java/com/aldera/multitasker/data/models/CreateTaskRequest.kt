package com.aldera.multitasker.data.models

data class CreateTaskRequest(
    val title: String?,
    val description: String?,
    val deadline: String?,
    val importance: Int?,
    val projectId: String?,
    val performerId: String
)
