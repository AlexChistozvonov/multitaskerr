package com.shakuro.aruuuba.core.utils

import com.squareup.moshi.Moshi

inline fun <reified T : Any> String.fromJsonOrNull(moshi: Moshi): T? = try {
    moshi.adapter(T::class.java).fromJson(this)
} catch (e: Exception) {
    null
}

inline fun <reified T : Any> T.toJson(moshi: Moshi): String? = try {
    moshi.adapter(T::class.java).toJson(this)
} catch (e: Exception) {
    null
}