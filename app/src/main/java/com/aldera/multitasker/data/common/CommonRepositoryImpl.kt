package com.aldera.multitasker.data.common

import android.content.Context
import android.net.Uri
import com.aldera.multitasker.core.ErrorMapper
import com.aldera.multitasker.core.di.IoDispatcher
import com.aldera.multitasker.core.network.Api
import com.aldera.multitasker.core.runLoading
import com.aldera.multitasker.data.utils.ImageRequestBody
import com.aldera.multitasker.domain.common.CommonRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody

class CommonRepositoryImpl @Inject constructor(
    private val networkService: Api,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val errorMapper: ErrorMapper,
    @ApplicationContext private val context: Context,
) : CommonRepository {
    override suspend fun uploadImage(uri: Uri) = withContext(coroutineDispatcher) {
        runLoading(errorMapper) {
            val requestFile = ImageRequestBody(
                "image/*".toMediaType(),
                context.contentResolver,
                uri
            )
            val requestData = MultipartBody.Part.createFormData(
                "file",
                uri.lastPathSegment.plus(".png"),
                requestFile
            )
            networkService.loadImage(requestData)
        }
    }
}
