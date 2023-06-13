package ru.yandex.school.todoapp.presentation.list.model

import ru.yandex.school.todoapp.domain.model.TodoItem

data class TodoListScreenState(
    val listItems: List<TodoItem>,
    val completedCount: Int,
    val isCompletedShowed: Boolean
) {

    companion object {

        val empty = TodoListScreenState(
            listItems = emptyList(),
            completedCount = 0,
            isCompletedShowed = false
        )
    }
}