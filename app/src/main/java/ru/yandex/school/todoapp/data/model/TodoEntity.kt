package ru.yandex.school.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.yandex.school.todoapp.data.database.converters.LocalDateConverter
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@TypeConverters(LocalDateConverter::class)
data class TodoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val text: String,
    val priority: TodoItemPriority,
    val isCompleted: Boolean,
    val createAt: LocalDate? = null,
    val deadline: LocalDate? = null,
    val modifiedAt: LocalDateTime? = null
)