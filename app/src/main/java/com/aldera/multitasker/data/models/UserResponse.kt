package com.aldera.multitasker.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    var id: String?,
    var createdAt: String?,
    var email: String?,
    var name: String?,
    var avatar: MultitaskerImage?
) : Parcelable

fun UserResponse.toPutUserRequest() = PutUserRequest(
    avatarId = this.avatar?.id,
    name = this.name,
    email = this.email
)
