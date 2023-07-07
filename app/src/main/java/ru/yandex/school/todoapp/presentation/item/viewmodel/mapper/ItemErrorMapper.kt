package ru.yandex.school.todoapp.presentation.item.viewmodel.mapper

import android.content.Context
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.data.model.error.ApiError
import ru.yandex.school.todoapp.data.model.error.AppError
import ru.yandex.school.todoapp.data.model.error.DbError
import ru.yandex.school.todoapp.data.model.error.NetworkError
import ru.yandex.school.todoapp.data.model.error.UnknownHostException

private const val BAD_REQUEST_CODE = 400
private const val UNAUTHORIZED_CODE = 401
private const val NOT_FOUND_CODE = 404
private const val INTERNAL_SERVER_ERROR_CODE = 500

class ItemErrorMapper(
    private val context: Context
) {

    fun map(error: Throwable): String {
        return if (error is ApiError) {
            when (error.status) {
                BAD_REQUEST_CODE -> when {
                    error.message!!.contains("unsynchronized") -> {
                        context.getString(R.string.exception_message_unsync_error)
                    }

                    error.message!!.contains("duplicate") -> {
                        context.getString(R.string.exception_message_duplicate_error)
                    }
                    else -> context.getString(R.string.exception_message_bad_request_error)
                }

                UNAUTHORIZED_CODE -> context.getString(R.string.exception_message_auth_error)
                NOT_FOUND_CODE -> context.getString(R.string.exception_message_not_found_error)
                INTERNAL_SERVER_ERROR_CODE -> context.getString(R.string.exception_message_server_error)
                else -> {
                    context.getString(R.string.exception_message_unknown_error)
                }
            }
        } else when (AppError.from(error)) {
            is UnknownHostException, NetworkError -> context.getString(R.string.exception_message_network_error)
            is DbError -> context.getString(R.string.exception_message_unknown_error)
            else -> context.getString(R.string.exception_message_unknown_error)
        }
    }
}
