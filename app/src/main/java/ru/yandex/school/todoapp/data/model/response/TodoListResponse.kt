package ru.yandex.school.todoapp.data.model.response

import com.google.gson.annotations.SerializedName
import ru.yandex.school.todoapp.data.model.request.TodoItemRemote

/**
 * Data class representing the response for a list of TodoItem from the API
 */
data class TodoListResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("list")
    val data: List<TodoItemRemote>,
    @SerializedName("revision")
    val revision: Int
)
