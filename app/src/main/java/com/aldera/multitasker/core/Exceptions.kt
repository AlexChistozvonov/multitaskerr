package com.aldera.multitasker.core

open class MultitaskerException : Exception()

class NetworkException : MultitaskerException()
class TimeoutException : MultitaskerException()
class GeneralException(val value: String? = "", val code: Int? = null) : MultitaskerException()

class ServerException(val errorCode: Int? = null) : MultitaskerException()
