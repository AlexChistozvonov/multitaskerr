package com.aldera.multitasker.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateSubtaskResponse(
    val title: String?,
    val description: String?,
    val deadline: String?,
    val importance: Int?,
    val isCompleted: Boolean?,
    val id: String?,
    val performer: Performer?,
    val createdAt: String?,
    val updatedAt: String?,
    val author: Performer?,
    val task: CreateTaskResponse?
) : Parcelable

fun CreateSubtaskResponse.toCreateSubtaskRequest() = this.performer?.id?.let {
    CreateSubtaskRequest(
        title = this.title,
        description = this.description,
        deadline = this.deadline,
        importance = this.importance,
        performerId = it,
        taskId = it
    )
}
