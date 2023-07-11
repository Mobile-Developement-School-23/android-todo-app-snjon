package ru.yandex.school.todoapp.presentation.util

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

private const val SWIPE_THRESHOLD_OFFSET = 0.1f

fun RecyclerView.setRecyclerViewItemTouchListener(
    swipeDirections: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
    swipeThreshold: Float = 0.3f
) {
    val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.ACTION_STATE_IDLE,
        swipeDirections
    ) {

        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = swipeThreshold

        override fun onChildDraw(
            canvas: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            if (viewHolder !is Swipeable) return
            val maxRange = viewHolder.itemView.width * (swipeThreshold - SWIPE_THRESHOLD_OFFSET)
            val x = if (dX < 0) {
                max(dX, -maxRange)
            } else {
                min(dX, maxRange)
            }

            when {
                dX == 0f && !isCurrentlyActive -> viewHolder.onChildDraw(0f, 0f)
                actionState == ItemTouchHelper.ACTION_STATE_SWIPE -> viewHolder.onChildDraw(x, dY)
                else -> super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
            (viewHolder as? Swipeable)?.onSwiped(swipeDirection)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            holder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = false
    }

    val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
    itemTouchHelper.attachToRecyclerView(this)
}

interface Swipeable {

    fun onChildDraw(dX: Float, dY: Float)

    fun onSwiped(direction: Int)
}
