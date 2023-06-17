package ru.yandex.school.todoapp.domain.model

import ru.yandex.school.todoapp.R

data class TodoItem(
    val id: String,
    val text: String,
    val priority: TodoItemPriority,
    val isCompleted: Boolean,
    val createAt: String? = null,
    val deadline: String? = null,
    val modifiedAt: String? = null
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