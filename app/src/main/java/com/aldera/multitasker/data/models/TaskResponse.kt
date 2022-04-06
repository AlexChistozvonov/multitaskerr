package com.aldera.multitasker.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskResponse(
    val isCompleted: Boolean?,
    val id: String?,
    val title: String?,
    var performer: Performer?,
    var color: String?,
    var importance: Int?
) : Parcelable
