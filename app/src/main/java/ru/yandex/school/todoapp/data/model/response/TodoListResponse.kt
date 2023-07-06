package ru.yandex.school.todoapp.data.model.response

import com.google.gson.annotations.SerializedName
import ru.yandex.school.todoapp.data.model.request.TodoItemRemote

data class TodoListResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("list")
    val data: List<TodoItemRemote>,
    @SerializedName("revision")
    val revision: Int
)
