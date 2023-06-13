package ru.yandex.school.todoapp.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItem

class TodoListAdapter(
    private val onInfoClick: ((TodoItem) -> Unit)? = null,
    private val onSwipeToCheck: ((TodoItem) -> Unit)? = null,
    private val onSwipeToDelete: ((TodoItem) -> Unit)? = null,
) : ListAdapter<TodoItem, TodoItemViewHolder>(TodoListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return TodoItemViewHolder(
            itemView = inflater.inflate(R.layout.view_todo_item, parent, false),
            onInfoClick = onInfoClick,
            onSwipeToCheck = onSwipeToCheck,
            onSwipeToDelete = onSwipeToDelete
        )
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}