package ru.yandex.school.todoapp.presentation.list.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.yandex.school.todoapp.domain.model.TodoItem

class TodoListDiffUtil : DiffUtil.ItemCallback<TodoItem>() {

    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}