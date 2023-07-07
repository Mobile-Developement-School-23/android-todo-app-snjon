package ru.yandex.school.todoapp.presentation.list.model

data class TodoListScreenState(
    val listItems: List<TodoItemListModel>,
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