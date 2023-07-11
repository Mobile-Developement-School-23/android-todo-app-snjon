package ru.yandex.school.todoapp.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a request to add a single TodoItem
 */
data class AddTodoRequest(
    @SerializedName("element")
    val element: TodoItemRemote
)
