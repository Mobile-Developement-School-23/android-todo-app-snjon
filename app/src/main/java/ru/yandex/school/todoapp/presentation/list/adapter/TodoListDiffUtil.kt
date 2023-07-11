package ru.yandex.school.todoapp.presentation.list.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.yandex.school.todoapp.presentation.list.model.TodoItemListModel

class TodoListDiffUtil : DiffUtil.ItemCallback<TodoItemListModel>() {

    override fun areItemsTheSame(oldItem: TodoItemListModel, newItem: TodoItemListModel): Boolean {
        return oldItem.payload.id == newItem.payload.id
    }

    override fun areContentsTheSame(oldItem: TodoItemListModel, newItem: TodoItemListModel): Boolean {
        return oldItem == newItem
    }
}
