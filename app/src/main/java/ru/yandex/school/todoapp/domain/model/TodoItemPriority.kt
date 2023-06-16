package ru.yandex.school.todoapp.domain.model

import ru.yandex.school.todoapp.R

enum class TodoItemPriority(
    val titleRes: Int,
    val colorRes: Int = R.color.color_gray,
    val imageRes: Int? = null
) {
    LOW(
        titleRes = R.string.todo_item_view_priority_low,
        imageRes = R.drawable.ic_low_priority
    ),
    DEFAULT(
        titleRes = R.string.todo_item_view_priority_default
    ),
    HIGH(
        titleRes = R.string.todo_item_view_priority_high,
        colorRes = R.color.color_red,
        imageRes = R.drawable.ic_high_priority
    )
}