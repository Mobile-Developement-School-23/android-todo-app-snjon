package ru.yandex.school.todoapp.presentation.item.priority

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import ru.yandex.school.todoapp.presentation.util.animateHighlight
import ru.yandex.school.todoapp.presentation.util.bind

/**
 * Bottom sheet dialog fragment for selecting priority
 */
class PriorityBottomSheet : BottomSheetDialogFragment(R.layout.priority_bottom_sheet) {

    private val defaultButton by bind<LinearLayout>(R.id.todo_item_priority_default)
    private val lowButton by bind<LinearLayout>(R.id.todo_item_priority_low)
    private val highButton by bind<LinearLayout>(R.id.todo_item_priority_high)

    private var priorityListener: Listener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        defaultButton.setOnClickListener {
            onPrioritySelected(TodoItemPriority.DEFAULT)
        }
        lowButton.setOnClickListener {
            onPrioritySelected(TodoItemPriority.LOW)
        }
        highButton.setOnClickListener {
            it.animateHighlight { onPrioritySelected(TodoItemPriority.HIGH) }
        }
    }

    private fun onPrioritySelected(priority: TodoItemPriority) {
        priorityListener?.onPrioritySelected(priority)
        dismiss()
    }

    companion object {

        fun show(fm: FragmentManager, listener: Listener) {
            PriorityBottomSheet()
                .apply { priorityListener = listener }
                .show(fm, PriorityBottomSheet::class.java.name)
        }
    }

    interface Listener {

        fun onPrioritySelected(priority: TodoItemPriority)
    }
}
