package com.aldera.multitasker.data.models

data class CreateTaskResponse(
    var title: String?,
    var description: String?,
    var deadline: String?,
    var importance: Int?,
    var isCompleted: Boolean?,
    var id: String?,
    var performer: Performer?
)

data class Performer(
    var id: String?,
    var createdAt: String?,
    var email: String?,
    var name: String?,
    var avatar: MultitaskerImage?
)
