package ru.yandex.school.todoapp.data.mapper

import ru.yandex.school.todoapp.data.model.database.TodoEntity
import ru.yandex.school.todoapp.data.model.request.TodoItemRemote
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class TodoItemMapper {

    /**
     * Mapper from TodoItem to TodoEntity
     * @param item [TodoItem]
     * @return [TodoEntity]
     */
    fun map(item: TodoItem): TodoEntity {
        return TodoEntity(
            id = item.id,
            text = item.text,
            priority = item.priority,
            isCompleted = item.isCompleted,
            createAt = item.createAt,
            deadline = item.deadline,
            modifiedAt = item.modifiedAt,
            isSync = item.isSync,
            hidden = item.hidden
        )
    }

    /**
     * Mapper from list TodoItem to list TodoEntity
     * @param items [List<TodoItem>]
     * @return [List<TodoEntity>]
     */
    fun map(items: List<TodoItem>): List<TodoEntity> {
        return items.map { item -> map(item) }
    }

    /**
     * Mapper from TodoItemRemote to TodoItem
     * @param item [TodoItemRemote]
     * @return [TodoItem]
     */
    fun mapFromResponse(item: TodoItemRemote): TodoItem {
        return TodoItem(
            id = item.id,
            text = item.text,
            priority = mapStringToPriority(item.priority),
            isCompleted = item.isCompleted,
            deadline = item.deadline?.let { LocalDate.ofEpochDay(it) },
            color = item.color,
            createAt = item.createAt.let { LocalDate.ofEpochDay(it) },
            modifiedAt = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(item.modifiedAt),
                ZoneOffset.UTC
            )
        )
    }

    /**
     * Mapper from TodoItem to TodoItemRemote
     * @param deviceId [String]
     * @param item [TodoItem]
     * @return [TodoItemRemote]
     */
    fun mapToRequest(deviceId: String, item: TodoItem): TodoItemRemote {
        return TodoItemRemote(
            id = item.id,
            text = item.text,
            priority = mapPriorityToString(item.priority),
            isCompleted = item.isCompleted,
            deadline = item.deadline?.toEpochDay(),
            color = item.color,
            createAt = item.createAt.toEpochDay(),
            modifiedAt = item.modifiedAt?.toEpochSecond(ZoneOffset.UTC)
                ?: item.createAt.toEpochDay(),
            updatedBy = deviceId
        )
    }

    /**
     * Mapper from TodoItemPriority to String representation of priority
     * @param priority [TodoItemPriority]
     * @return [String]
     */
    private fun mapPriorityToString(priority: TodoItemPriority): String {
        return when (priority) {
            TodoItemPriority.LOW -> "low"
            TodoItemPriority.DEFAULT -> "basic"
            TodoItemPriority.HIGH -> "important"
        }
    }

    /**
     * Mapper from String representation of priority to TodoItemPriority
     * @param priorityString [String]
     * @return [TodoItemPriority]
     */
    private fun mapStringToPriority(priorityString: String): TodoItemPriority {
        return when (priorityString) {
            "low" -> TodoItemPriority.LOW
            "basic" -> TodoItemPriority.DEFAULT
            "important" -> TodoItemPriority.HIGH
            else -> TodoItemPriority.DEFAULT
        }
    }
}
