package ru.yandex.school.todoapp.presentation.list.viewmodel.mapper

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.presentation.item.viewmodel.mapper.TodoItemDateMapper
import ru.yandex.school.todoapp.presentation.list.model.TodoItemListModel
import ru.yandex.school.todoapp.presentation.util.getColorExt
import ru.yandex.school.todoapp.presentation.util.getDrawableExt

class TodoListItemMapper(
    private val context: Context,
    private val dataMapper: TodoItemDateMapper
) {

    fun map(todoItem: TodoItem): TodoItemListModel {
        return TodoItemListModel(
            text = todoItem.text,
            textColor = context.getColorExt(todoItem.getTextColorRes()),
            textPaintFlags = getPaintFlags(todoItem),
            isChecked = todoItem.isCompleted,
            checkboxTint = ColorStateList.valueOf(context.getColorExt(todoItem.getIndicatorColorRes())),
            priorityImage = todoItem.priority.imageRes?.let { context.getDrawableExt(it) },
            deadlineDate = todoItem.deadline?.let { dataMapper.map(it) },
            payload = todoItem
        )
    }

    private fun getPaintFlags(todoItem: TodoItem): Int {
        return if (todoItem.isCompleted) {
            Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}
