package com.aldera.multitasker.ui.exception

import android.content.Context
import com.aldera.multitasker.R
import com.aldera.multitasker.core.GeneralException
import com.aldera.multitasker.core.NetworkException
import com.aldera.multitasker.core.ServerException
import com.aldera.multitasker.core.TimeoutException

@Suppress("UnusedPrivateMember", "UNUSED_EXPRESSION")
class UIExceptionMapper {
    fun titleMapper(context: Context, throwable: Throwable?): String {
        return when (throwable) {
            is NetworkException -> context.resources.getString(R.string.error_network)
            is ServerException -> context.resources.getString(R.string.error_server)
            is TimeoutException -> context.resources.getString(R.string.error_smt_go_wrong)
            is GeneralException -> throwable.message.toString()
            else -> context.resources.getString(R.string.error_smt_go_wrong)
        }
    }

    fun subtitleMapper(context: Context, throwable: Throwable?): String {
        return when (throwable) {
            is NetworkException -> context.resources.getString(R.string.error_network_subtitle)
            is ServerException -> context.resources.getString(R.string.error_server_subtitle)
            is TimeoutException -> context.resources.getString(R.string.error_smt_go_wrong_subtitle)
            is GeneralException -> ""
            else -> context.resources.getString(R.string.error_smt_go_wrong_subtitle)
        }
    }
}
