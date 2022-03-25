package com.aldera.multitasker.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectResponse(
    val title: String?,
    val id: String?
) : Parcelable
