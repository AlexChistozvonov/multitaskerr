package com.aldera.multitasker.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskResponse(
    val isCompleted: Boolean?,
    val id: String?,
    val title: String?,
    val performer: Performer?,
    val color: String?,
    val importance: Int?
) : Parcelable
