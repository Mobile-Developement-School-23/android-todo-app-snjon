package ru.yandex.school.todoapp.data.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.yandex.school.todoapp.data.database.converters.LocalDateTypeConverter
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Entity class representing a TodoItem in the database
 */
@Entity
@TypeConverters(LocalDateTypeConverter::class)
data class TodoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val text: String,
    val priority: TodoItemPriority,
    val isCompleted: Boolean,
    val createAt: LocalDate,
    val deadline: LocalDate? = null,
    val modifiedAt: LocalDateTime? = null,
    val isSync: Boolean,
    val hidden: Boolean
)
