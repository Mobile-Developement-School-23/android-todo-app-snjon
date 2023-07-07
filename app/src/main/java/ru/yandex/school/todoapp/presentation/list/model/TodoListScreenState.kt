package ru.yandex.school.todoapp.presentation.list.model

/**
 * Represents the state of the TodoItem list screen
 */
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
