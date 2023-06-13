package ru.yandex.school.todoapp.presentation.list.adapter

import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.presentation.util.Swipeable
import ru.yandex.school.todoapp.presentation.util.bind

class TodoItemViewHolder(
    itemView: View,
    private val onInfoClick: ((TodoItem) -> Unit)? = null,
    private val onSwipeToCheck: ((TodoItem) -> Unit)? = null,
    private val onSwipeToDelete: ((TodoItem) -> Unit)? = null,
) : RecyclerView.ViewHolder(itemView), Swipeable {

    private val card by bind<ConstraintLayout>(R.id.todo_item_card)
    private val checkbox by bind<CheckBox>(R.id.todo_item_check)
    private val text by bind<TextView>(R.id.todo_item_text)
    private val info by bind<ImageView>(R.id.todo_item_info)

    private var item: TodoItem? = null

    fun bind(todoItem: TodoItem) {
        card.translationX = 0f
        item = todoItem
        checkbox.isChecked = todoItem.isCompleted
        text.text = todoItem.text
        text.paintFlags =
            if (todoItem.isCompleted) text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        info.setOnClickListener { onInfoClick?.invoke(todoItem) }
    }

    override fun onChildDraw(dX: Float, dY: Float) {
        card.translationX = dX
    }

    override fun onSwiped(direction: Int) {
        val todoItem = item ?: return

        when (direction) {
            ItemTouchHelper.LEFT -> onSwipeToDelete?.invoke(todoItem)
            ItemTouchHelper.RIGHT -> onSwipeToCheck?.invoke(todoItem)
        }
    }
}