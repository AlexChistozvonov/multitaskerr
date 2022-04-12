package com.aldera.multitasker.data.models

data class UserTaskResponse(
    val isCompleted: Boolean?,
    val id: String?,
    val title: String?,
    val performer: Performer?,
    val color: String?,
    val importance: Int?
)
