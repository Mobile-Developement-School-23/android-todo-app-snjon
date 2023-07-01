package ru.yandex.school.todoapp.data.model.request

import com.google.gson.annotations.SerializedName

data class AddTodoRequest(
    @SerializedName("element")
    val element: TodoItemRemote
)