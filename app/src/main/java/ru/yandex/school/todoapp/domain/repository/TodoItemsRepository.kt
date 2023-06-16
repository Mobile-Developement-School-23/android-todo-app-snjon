package ru.yandex.school.todoapp.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.yandex.school.todoapp.domain.model.TodoItem

interface TodoItemsRepository {

    fun getTodoItems(): StateFlow<Map<String, TodoItem>>

    fun getTodoById(id: String): TodoItem?

    fun saveTodoItem(item: TodoItem)

    fun deleteTodoItem(item: TodoItem)
}