package ru.yandex.school.todoapp.domain.model

import ru.yandex.school.todoapp.R
import java.time.LocalDate

data class TodoItem(
    val id: String,
    val text: String,
    val priority: TodoItemPriority,
    val isCompleted: Boolean,
    val createAt: LocalDate? = null,
    val deadline: LocalDate? = null,
    val modifiedAt: LocalDate? = null
) {

    fun getIndicatorColorRes(): Int {
        return when {
            isCompleted -> R.color.color_green
            else -> priority.colorRes
        }
    }

    fun getTextColorRes(): Int {
        return if (isCompleted) R.color.label_tertiary else R.color.label_primary
    }

    companion object {
        val empty = TodoItem(
            id = "0",
            text = "",
            priority = TodoItemPriority.DEFAULT,
            isCompleted = false
        )
    }
}