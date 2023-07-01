package ru.yandex.school.todoapp.data.model.response

import com.google.gson.annotations.SerializedName
import ru.yandex.school.todoapp.data.model.request.TodoItemRemote

data class TodoItemResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("element")
    val data: TodoItemRemote,
    @SerializedName("revision")
    val revision: Int
)