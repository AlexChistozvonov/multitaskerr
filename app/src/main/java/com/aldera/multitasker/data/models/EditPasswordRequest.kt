package com.aldera.multitasker.data.models

data class EditPasswordRequest(
    val oldPassword: String?,
    val newPassword: String?
)
