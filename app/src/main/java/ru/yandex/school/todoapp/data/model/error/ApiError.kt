package ru.yandex.school.todoapp.data.model.error

import android.database.SQLException
import java.io.IOException
import java.lang.RuntimeException

sealed class AppError(var code: String) : RuntimeException() {
    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is UnknownHostException -> UnknownHostException
            is IOException -> NetworkError
            else -> UnknownError
        }
    }
}

class ApiError(val status: Int, code: String) : AppError(code)
object NetworkError : AppError("error_network")
object DbError : AppError("error_db")
object UnknownError : AppError("error_unknown")
object UnknownHostException: AppError("error_network")
