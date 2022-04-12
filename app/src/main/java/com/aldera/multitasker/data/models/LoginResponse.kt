package com.aldera.multitasker.data.models

data class LoginResponse(
    val accessToken: String?,
    val tokenType: String?,
    val refreshToken: String?
)
