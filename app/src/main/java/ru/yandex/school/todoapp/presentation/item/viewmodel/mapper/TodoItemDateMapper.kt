package ru.yandex.school.todoapp.presentation.item.viewmodel.mapper

import android.content.Context
import ru.yandex.school.todoapp.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Mapper for formatting TodoItem dates to strings
 * @param context The context used for retrieving string resources
 */
class TodoItemDateMapper(private val context: Context) {
    private val dateFormatter by lazy { createFormatter() }

    fun map(date: LocalDate): String {
        return date.format(dateFormatter)
    }

    fun map(date: LocalDateTime): String {
        return date.format(dateFormatter)
    }

    private fun createFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(
            context.getString(R.string.todo_item_view_date_pattern),
            Locale.getDefault()
        )
    }
}
