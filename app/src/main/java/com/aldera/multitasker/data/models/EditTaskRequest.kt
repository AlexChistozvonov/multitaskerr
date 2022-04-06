package com.aldera.multitasker.data.models

data class EditTaskRequest(
    val title: String?,
    val description: String?,
    val deadline: String?,
    val importance: Int?
)
