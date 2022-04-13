package com.aldera.multitasker.data.models

data class CreateCategoryResponse(
    val title: String?,
    val color: String?,
    val description: String?,
    val id: String?,
    val projectsCount: Int?
)
