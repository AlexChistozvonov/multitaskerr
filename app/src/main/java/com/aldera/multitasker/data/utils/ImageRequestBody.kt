package com.aldera.multitasker.data.utils

import android.content.ContentResolver
import android.net.Uri
import java.io.IOException
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

class ImageRequestBody(
    private val contentType: MediaType,
    private val contentResolver: ContentResolver,
    private val uri: Uri
) : RequestBody() {
    var contentLength: Long = -1

    override fun contentType() = contentType

    override fun contentLength(): Long = contentLength

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val input = contentResolver.openInputStream(uri)
        input?.use {
            contentLength = it.available().toLong()
            sink.writeAll(it.source())
        } ?: throw IOException("Could not open $uri")
    }
}
