package com.aldera.multitasker.data.models

data class LoginResponse(
    var accessToken: String? = null,
    var tokenType: String? = null,
    var refreshToken: String? = null,
)
