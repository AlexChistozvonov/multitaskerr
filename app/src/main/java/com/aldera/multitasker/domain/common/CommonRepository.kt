package com.aldera.multitasker.domain.common

import android.net.Uri
import com.aldera.multitasker.core.LoadingResult
import com.aldera.multitasker.data.models.MultitaskerImage

interface CommonRepository {
    suspend fun uploadImage(uri: Uri): LoadingResult<MultitaskerImage>
}
