package com.aldera.multitasker.data.models

data class UserResponse(
    var id: String?,
    var createdAt: String?,
    var email: String?,
    var name: String?,
    var avatar: UserAvatar?
)
