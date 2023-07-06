package ru.yandex.school.todoapp.presentation.authorization.viewmodel.mapper

import android.content.Context
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.data.model.error.ApiError
import ru.yandex.school.todoapp.data.model.error.AppError
import ru.yandex.school.todoapp.data.model.error.NetworkError
import ru.yandex.school.todoapp.data.model.error.UnknownHostException

class AuthErrorMapper(
    private val context: Context
) {

    fun map(error: Throwable): String {

        return if (error is ApiError) {
            when (error.status) {
                400 -> "Неверный запрос: ${error.status} ${error.code}"
                401 -> context.getString(R.string.exception_message_token_error)
                500 -> context.getString(R.string.exception_message_server_error)
                else -> {
                    context.getString(R.string.exception_message_unknown_error)
                }
            }
        } else {
            when (AppError.from(error)) {
                is UnknownHostException, NetworkError -> context.getString(R.string.exception_message_network_error)
                else -> context.getString(R.string.exception_message_unknown_error)
            }
        }
    }
}