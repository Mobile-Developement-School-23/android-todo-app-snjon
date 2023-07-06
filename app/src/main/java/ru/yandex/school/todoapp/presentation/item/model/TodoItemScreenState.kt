package ru.yandex.school.todoapp.presentation.item.model

import ru.yandex.school.todoapp.R

data class TodoItemScreenState(
    val text: String = "",
    val priorityRes: Int = R.string.todo_item_view_priority_default,
    val deadlineDate: String? = null,
    val modifiedDate: String? = null
)