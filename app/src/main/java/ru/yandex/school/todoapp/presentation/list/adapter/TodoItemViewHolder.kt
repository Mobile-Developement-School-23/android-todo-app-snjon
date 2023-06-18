package ru.yandex.school.todoapp.presentation.list.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.presentation.util.Swipeable
import ru.yandex.school.todoapp.presentation.util.bind
import ru.yandex.school.todoapp.presentation.util.setColor
import ru.yandex.school.todoapp.presentation.util.visibleOrGone

class TodoItemViewHolder(
    itemView: View,
    private val onInfoClick: ((TodoItem) -> Unit)? = null,
    private val onActionLongClick: ((TodoItem, Int) -> Unit)? = null,
    private val onSwipeToCheck: ((TodoItem) -> Unit)? = null,
    private val onSwipeToDelete: ((TodoItem) -> Unit)? = null,
) : RecyclerView.ViewHolder(itemView), Swipeable {

    private val card by bind<ConstraintLayout>(R.id.todo_item_card)
    private val checkbox by bind<CheckBox>(R.id.todo_item_check)
    private val text by bind<TextView>(R.id.todo_item_text)
    private val priority by bind<ImageView>(R.id.todo_item_priority)
    private val itemText by bind<TextView>(R.id.todo_item_text)

    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f

    private var item: TodoItem? = null


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(todoItem: TodoItem) {
        card.translationX = 0f
        item = todoItem
        checkbox.isChecked = todoItem.isCompleted
        text.text = todoItem.text
        text.paintFlags =
            if (todoItem.isCompleted) text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

        itemText.setColor(if (todoItem.isCompleted) R.color.label_tertiary else R.color.label_primary)
        checkbox.setColor(todoItem.getIndicatorColorRes())
        priority.visibleOrGone(todoItem.priority.imageRes != null)
        todoItem.priority.imageRes?.let { priority.setImageResource(it) }

        card.setOnClickListener { onInfoClick?.invoke(todoItem) }

        card.setOnTouchListener(View.OnTouchListener { _, event ->
            lastTouchX = event.x
            lastTouchY = event.y
            return@OnTouchListener false
        })

        card.setOnLongClickListener {
            showActionMenu(card, lastTouchX.toInt(), lastTouchY.toInt(), todoItem)
            true
        }

        checkbox.setOnClickListener {
            onSwipeToCheck?.invoke(todoItem)
        }
    }


    private fun showActionMenu(view: View, x: Int, y: Int, todoItem: TodoItem) {
        val layoutInflater = LayoutInflater.from(view.context)
        val popupWindow = PopupWindow(view.context)
        val menuView = layoutInflater.inflate(R.layout.action_menu_layout, card, false)
        popupWindow.contentView = menuView
        popupWindow.isFocusable = true

        val completeItem = menuView.findViewById<TextView>(R.id.menu_action_completed)
        val editItem = menuView.findViewById<TextView>(R.id.menu_action_edit)
        val deleteItem = menuView.findViewById<TextView>(R.id.menu_action_delete)

        if (todoItem.isCompleted) {
            completeItem.setText(R.string.todo_list_action_not_complete)
        } else {
            completeItem.setText(R.string.todo_list_action_complete)
        }

        var selectedItemResId: Int? = null

        completeItem.setOnClickListener { itemView ->
            selectedItemResId = itemView.id
            popupWindow.dismiss()
        }

        editItem.setOnClickListener { itemView ->
            selectedItemResId = itemView.id
            popupWindow.dismiss()
        }

        deleteItem.setOnClickListener { itemView ->
            selectedItemResId = itemView.id
            popupWindow.dismiss()
        }

        popupWindow.setOnDismissListener {
            selectedItemResId?.let { onActionLongClick?.invoke(todoItem, it) }
        }

        popupWindow.showAsDropDown(view, x, y)
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