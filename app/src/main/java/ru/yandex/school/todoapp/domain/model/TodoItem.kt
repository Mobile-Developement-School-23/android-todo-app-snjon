package ru.yandex.school.todoapp.domain.model

import ru.yandex.school.todoapp.R
import java.time.LocalDate
import java.time.LocalDateTime

data class TodoItem(
    val id: String,
    val text: String,
    val priority: TodoItemPriority,
    val isCompleted: Boolean,
    val color: String? = null,
    val createAt: LocalDate,
    val deadline: LocalDate? = null,
    val modifiedAt: LocalDateTime? = null,
    val isSync: Boolean = false
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
            isCompleted = false,
            createAt = LocalDate.now()
        )
    }
}