package com.aldera.multitasker.data.models

data class CreateCategoryResponse(
    var title: String?,
    var color: String?,
    var description: String?,
    var id: String?,
    var projectsCount: Int?
)
