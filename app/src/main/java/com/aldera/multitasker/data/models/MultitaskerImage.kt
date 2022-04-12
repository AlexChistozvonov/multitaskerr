package com.aldera.multitasker.data.models

import android.os.Parcelable
import com.aldera.multitasker.BuildConfig
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultitaskerImage(
    val id: String,
    val path: String,
    val name: String,
    val type: String,
) : Parcelable

fun MultitaskerImage.imageUrl(): String {
    return BuildConfig.SERVER_URL + path
}
