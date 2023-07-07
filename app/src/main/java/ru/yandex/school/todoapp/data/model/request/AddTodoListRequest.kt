package ru.yandex.school.todoapp.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a request to add a list of TodoItem
 */
data class AddTodoListRequest(
    @SerializedName("list")
    val list: List<TodoItemRemote>
)
