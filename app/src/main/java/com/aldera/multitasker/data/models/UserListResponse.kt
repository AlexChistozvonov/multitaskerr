package com.aldera.multitasker.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserListResponse(
    val id: String?,
    val email: String?,
    val avatar: MultitaskerImage?
) : Parcelable
