package ru.yandex.school.todoapp.data.model.error

import android.database.SQLException
import java.io.IOException
import java.net.UnknownHostException
import java.lang.RuntimeException

/**
 * Base class for application errors
 * @property code [String]
 */
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

/**
 * Error class representing an API error with a specific status code
 * @property status [Int]
 * @property code [String]
 */
class ApiError(val status: Int, code: String) : AppError(code)

/**
 * Error class representing a network error
 */
object NetworkError : AppError("error_network")

/**
 * Error class representing a database error
 */
object DbError : AppError("error_db")

/**
 * Error class representing an unknown error
 */
object UnknownError : AppError("error_unknown")

/**
 * Error class representing an unknown host error (network-related)
 */
object UnknownHostException: AppError("error_network")
