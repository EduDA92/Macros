package com.example.macrostracker.data.util


sealed class ApiResult <out T> ( val data: T?, val message:String?) {

    data class Success<out R>(val _data: R?): ApiResult<R>(
        data = _data,
        message = null
    )

    data class Error(val exception: String): ApiResult<Nothing>(
        data = null,
        message = exception
    )
}
