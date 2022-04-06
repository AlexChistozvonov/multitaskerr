package com.aldera.multitasker.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateTaskResponse(
    var title: String?,
    var description: String?,
    var deadline: String?,
    var importance: Int?,
    var isCompleted: Boolean?,
    var id: String?,
    var performer: Performer?,
    var createdAt: String?,
    var updatedAt: String?,
    var author: Performer?
) : Parcelable

@Parcelize
data class Performer(
    var id: String?,
    var createdAt: String?,
    var email: String?,
    var name: String?,
    var avatar: MultitaskerImage?
) : Parcelable
