package ru.yandex.school.todoapp.data.mapper

import ru.yandex.school.todoapp.data.model.TodoEntity
import ru.yandex.school.todoapp.domain.model.TodoItem

class TodoEntityMapper {

    fun map(entity: TodoEntity): TodoItem {
        return TodoItem(
            id = entity.id,
            text = entity.text,
            priority = entity.priority,
            isCompleted = entity.isCompleted,
            createAt = entity.createAt,
            deadline = entity.deadline,
            modifiedAt = entity.modifiedAt
        )
    }

    fun map(entities: List<TodoEntity>): List<TodoItem> {
        return entities.map { entity -> map(entity) }
    }
}