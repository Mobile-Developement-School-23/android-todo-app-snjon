package ru.yandex.school.todoapp.data.mapper

import ru.yandex.school.todoapp.data.model.database.TodoEntity
import ru.yandex.school.todoapp.domain.model.TodoItem

class TodoEntityMapper {

    /**
     * Mapper from TodoEntity to TodoItem
     * @param entity [TodoEntity]
     * @return [TodoItem]
     */
    fun map(entity: TodoEntity): TodoItem {
        return TodoItem(
            id = entity.id,
            text = entity.text,
            priority = entity.priority,
            isCompleted = entity.isCompleted,
            createAt = entity.createAt,
            deadline = entity.deadline,
            modifiedAt = entity.modifiedAt,
            isSync = entity.isSync
        )
    }

    /**
     * Mapper from list TodoEntity to list TodoItem
     * @param entities [List<TodoEntity>]
     * @return [TodoItem]
     */
    fun map(entities: List<TodoEntity>): List<TodoItem> {
        return entities.map { entity -> map(entity) }
    }
}
