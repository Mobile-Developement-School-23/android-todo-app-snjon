package ru.yandex.school.todoapp.presentation.list.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItem

class TodoListAdapter(
    private val onInfoClick: ((TodoItem) -> Unit)? = null,
    private val onLongClick: ((TodoItem, Int) -> Unit)? = null,
    private val onSwipeToCheck: ((TodoItem) -> Unit)? = null,
    private val onSwipeToDelete: ((TodoItem) -> Unit)? = null,
) : ListAdapter<TodoItem, TodoItemViewHolder>(TodoListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return TodoItemViewHolder(
            itemView = inflater.inflate(R.layout.view_todo_item, parent, false),
            onInfoClick = onInfoClick,
            onLongClick = onLongClick,
            onSwipeToCheck = onSwipeToCheck,
            onSwipeToDelete = onSwipeToDelete
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}