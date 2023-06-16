package ru.yandex.school.todoapp.domain.model

import ru.yandex.school.todoapp.R

data class TodoItem(
    val id: String,
    val text: String,
    val priority: TodoItemPriority,
    val isCompleted: Boolean,
    val createDate: String? = null,
    val deadlineDate: String? = null,
    val changeDate: String? = null
) {

    fun getIndicatorColorRes(): Int {
        return when {
            isCompleted -> R.color.color_green
            else -> priority.colorRes
        }
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