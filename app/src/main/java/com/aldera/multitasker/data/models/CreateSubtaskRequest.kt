package com.aldera.multitasker.data.models

data class CreateSubtaskRequest(
    val title: String?,
    val description: String?,
    val deadline: String?,
    val importance: Int?,
    val taskId: String,
    val performerId: String
)
