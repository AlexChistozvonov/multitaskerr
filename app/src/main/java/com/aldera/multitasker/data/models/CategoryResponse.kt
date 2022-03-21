package com.aldera.multitasker.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryResponse(
    val title: String?,
    val color: String?,
    val description: String?,
    val id: String?,
    val projectsCount: Int?
) : Parcelable
