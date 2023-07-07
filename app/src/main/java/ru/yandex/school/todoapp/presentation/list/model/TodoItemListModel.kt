package ru.yandex.school.todoapp.presentation.list.model

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import ru.yandex.school.todoapp.domain.model.TodoItem

data class TodoItemListModel(
    val text: String,
    val textColor: Int,
    val textPaintFlags: Int,
    val isChecked: Boolean,
    val checkboxTint: ColorStateList,
    val priorityImage: Drawable?,
    val deadlineDate: String?,
    val payload: TodoItem
)
