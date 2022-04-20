package com.aldera.multitasker.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateTaskResponse(
    val title: String?,
    val description: String?,
    val deadline: String?,
    val importance: Int?,
    val isCompleted: Boolean?,
    val id: String?,
    val performer: Performer?,
    val createdAt: String?,
    val updatedAt: String?,
    val author: Performer?
) : Parcelable

fun CreateTaskResponse.toCreateTaskRequest() = this.performer?.id?.let {
    CreateTaskRequest(
        title = this.title,
        description = this.description,
        deadline = this.deadline,
        importance = this.importance,
        performerId = it,
        projectId = this.id
    )
}

@Parcelize
data class Performer(
    val id: String?,
    val createdAt: String?,
    val email: String?,
    val name: String?,
    val avatar: MultitaskerImage?
) : Parcelable
