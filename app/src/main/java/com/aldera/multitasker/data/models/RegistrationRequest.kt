package com.aldera.multitasker.data.models

data class RegistrationRequest(
    val email: String? = null,
    val password: String? = null,
    val password2: String? = null
)
