package ru.yandex.school.todoapp.domain.repository

import ru.yandex.school.todoapp.domain.model.TodoItem

interface TodoItemsRepository {

    fun getTodoItems(): List<TodoItem>

    fun addTodoItem(item: TodoItem)
}