package com.aldera.multitasker.data.models

data class RecoveryPasswordCreateRequest(
    val key: String?,
    val password: String?,
    val password2: String?
)
