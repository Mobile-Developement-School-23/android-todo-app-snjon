package ru.yandex.school.todoapp.presentation.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.presentation.list.model.TodoItemListModel
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
    private val priority by bind<ImageView>(R.id.todo_item_priority_subtitle)
    private val itemText by bind<TextView>(R.id.todo_item_text)
    private val deadline by bind<TextView>(R.id.deadline_date)

    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f

    private var item: TodoItemListModel? = null

    @SuppressLint("ClickableViewAccessibility")
    fun bind(todoItem: TodoItemListModel) {
        card.translationX = 0f
        item = todoItem

        text.apply {
            text = todoItem.text
            setTextColor(todoItem.textColor)
            paintFlags = if (todoItem.isChecked) {
                paintFlags or todoItem.textPaintFlags
            } else {
                paintFlags and todoItem.textPaintFlags
            }
        }

        checkbox.apply {
            isChecked = todoItem.isChecked
            buttonTintList = todoItem.checkboxTint
        }

        deadline.apply {
            deadline.visibleOrGone(todoItem.deadlineDate.isNullOrEmpty().not())
            deadline.text =
                deadline.context.getString(R.string.todo_list_date_before, todoItem.deadlineDate)
        }

        itemText.setColor(if (todoItem.payload.isCompleted) R.color.label_tertiary else R.color.label_primary)
        priority.visibleOrGone(todoItem.priorityImage != null)
        priority.setImageDrawable(todoItem.priorityImage)

        card.setOnClickListener { onInfoClick?.invoke(todoItem.payload) }

        card.setOnTouchListener(View.OnTouchListener { _, event ->
            lastTouchX = event.x
            lastTouchY = event.y
            return@OnTouchListener false
        })

        card.setOnLongClickListener {
            showActionMenu(card, lastTouchX.toInt(), lastTouchY.toInt(), todoItem.payload)
            true
        }

        checkbox.setOnClickListener {
            onSwipeToCheck?.invoke(todoItem.payload)
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
            ItemTouchHelper.LEFT -> onSwipeToDelete?.invoke(todoItem.payload)
            ItemTouchHelper.RIGHT -> onSwipeToCheck?.invoke(todoItem.payload)
        }
    }
}