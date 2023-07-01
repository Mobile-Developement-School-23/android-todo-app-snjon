package ru.yandex.school.todoapp.data.mapper

import ru.yandex.school.todoapp.data.model.TodoEntity
import ru.yandex.school.todoapp.domain.model.TodoItem

class TodoItemMapper {

    fun map(item: TodoItem): TodoEntity {
        return TodoEntity(
            id = item.id,
            text = item.text,
            priority = item.priority,
            isCompleted = item.isCompleted,
            createAt = item.createAt,
            deadline = item.deadline,
            modifiedAt = item.modifiedAt,
            isSync = item.isSync
        )
    }

    fun map(items: List<TodoItem>): List<TodoEntity> {
        return items.map { item -> map(item) }
    }
}