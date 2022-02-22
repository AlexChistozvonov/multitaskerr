package com.aldera.multitasker.core

import android.net.ParseException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import timber.log.Timber

suspend fun <T : Any> runLoading(
    mapper: ErrorMapper,
    block: suspend () -> T
): LoadingResult<T> {
    return try {
        LoadingResult.Success(block())
    } catch (error: Throwable) {
        Timber.e(error, "runLoading error:")
        LoadingResult.Error(mapper.mapException(error))
    }
}

sealed class LoadingResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : LoadingResult<T>()
    data class Error(val exception: Exception) : LoadingResult<Nothing>()
}

class ErrorMapper @Inject constructor() {

    fun mapException(throwable: Throwable): MultitaskerException {
        return when (throwable) {
            is Exception -> invoke(throwable)
            else -> ServerException()
        }
    }
    operator fun invoke(exception: Exception): MultitaskerException {
        return when (exception) {
            is UnknownHostException -> NetworkException()
            is SocketTimeoutException -> TimeoutException()
            is ParseException -> ServerException()
            else -> ServerException()
        }
    }
}
