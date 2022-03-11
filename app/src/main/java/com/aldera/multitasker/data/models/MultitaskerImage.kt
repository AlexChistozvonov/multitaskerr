package com.aldera.multitasker.data.models

import android.os.Parcelable
import com.aldera.multitasker.BuildConfig
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultitaskerImage(
    var id: String,
    var path: String,
    var name: String,
    var type: String,
) : Parcelable

fun MultitaskerImage.imageUrl(): String {
    return BuildConfig.SERVER_URL + path
}
