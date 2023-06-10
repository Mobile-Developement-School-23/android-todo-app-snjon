package ru.yandex.school.todoapp.data.repository

import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository

class TodoItemsRepositoryImpl : TodoItemsRepository {

    override fun getTodoItems(): List<TodoItem> {
        return emptyList()
    }

    override fun addTodoItem(item: TodoItem) {
        // TODO
    }
}