package ru.yandex.school.todoapp.data.model.request

import com.google.gson.annotations.SerializedName

data class TodoItemRemote(
    @SerializedName("id")
    val id: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("importance")
    val priority: String,
    @SerializedName("deadline")
    val deadline: Long?,
    @SerializedName("done")
    val isCompleted: Boolean,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val createAt: Long,
    @SerializedName("changed_at")
    val modifiedAt: Long,
    @SerializedName("last_updated_by")
    val updatedBy: String
)
