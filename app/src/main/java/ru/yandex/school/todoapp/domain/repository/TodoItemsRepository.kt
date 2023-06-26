package ru.yandex.school.todoapp.domain.repository

import ru.yandex.school.todoapp.domain.model.TodoItem

interface TodoItemsRepository {

    suspend fun getTodoItems(): List<TodoItem>

    suspend fun getTodoById(id: String): TodoItem?

    suspend fun saveTodoItem(item: TodoItem)

    suspend fun saveTodoItems(items: List<TodoItem>)

    suspend fun deleteTodoItem(item: TodoItem)
}