package ru.yandex.school.todoapp.presentation.item.viewmodel.mapper

import android.content.Context
import ru.yandex.school.todoapp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class TodoItemDateMapper(private val context: Context) {

    private val dateFormatter by lazy { createFormatter() }

    fun map(date: LocalDate): String {
        return date.format(dateFormatter)
    }

    private fun createFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(
            context.getString(R.string.todo_item_view_date_pattern),
            Locale.getDefault()
        )
    }
}