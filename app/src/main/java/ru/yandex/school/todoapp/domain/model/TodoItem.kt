package ru.yandex.school.todoapp.domain.model

data class TodoItem(
    val id: String,
    val text: String,
    val priority: TodoItemPriority,
    val isCompleted: Boolean,
    val createDate: String,
    val deadLineDate: String?,
    val changeDate: String?
)