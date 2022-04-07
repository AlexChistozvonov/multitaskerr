package com.aldera.multitasker.data.models

data class CreateSubtaskResponse(
    var title: String?,
    var description: String?,
    var deadline: String?,
    var importance: Int?,
    var isCompleted: Boolean?,
    var id: String?,
    var performer: Performer?,
    var createdAt: String?,
    var updatedAt: String?,
    var author: Performer?,
    var task: CreateTaskResponse?
)
