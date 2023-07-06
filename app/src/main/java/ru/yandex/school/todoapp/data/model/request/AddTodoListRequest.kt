package ru.yandex.school.todoapp.data.model.request

import com.google.gson.annotations.SerializedName

data class AddTodoListRequest(
    @SerializedName("list")
    val list: List<TodoItemRemote>
)