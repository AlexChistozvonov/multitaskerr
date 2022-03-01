package com.aldera.multitasker.data.models

data class LoginResponse(
    var accessToken: String?,
    var tokenType: String?,
    var refreshToken: String?
)
