package ru.yandex.school.todoapp.presentation.item

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import ru.yandex.school.todoapp.presentation.datetime.DatePickerDialog
import ru.yandex.school.todoapp.presentation.item.model.TodoItemScreenState
import ru.yandex.school.todoapp.presentation.item.viewmodel.TodoItemViewModel
import ru.yandex.school.todoapp.presentation.navigation.KEY_TODO_ITEM_ID
import ru.yandex.school.todoapp.presentation.util.bind
import ru.yandex.school.todoapp.presentation.util.repeatOnCreated
import ru.yandex.school.todoapp.presentation.util.setButtonColor
import ru.yandex.school.todoapp.presentation.util.show
import ru.yandex.school.todoapp.presentation.util.visibleOrGone

class TodoItemFragment : Fragment(R.layout.fragment_todo_item) {

    private val viewModel: TodoItemViewModel by viewModel {
        parametersOf(
            requireArguments().getString(KEY_TODO_ITEM_ID)
        )
    }

    private val editText by bind<TextView>(R.id.todo_item_text)
    private val saveButton by bind<TextView>(R.id.todo_item_save)

    private val priorityCard by bind<LinearLayout>(R.id.todo_item_priority_item)
    private val priority by bind<TextView>(R.id.todo_item_priority_subtitle)

    private val deadlineCard by bind<ConstraintLayout>(R.id.todo_item_deadline_item)
    private val deadlineDate by bind<TextView>(R.id.todo_item_deadline_subtitle)
    private val deadlineSwitch by bind<SwitchMaterial>(R.id.todo_item_deadline_switch)

    private val deleteButton by bind<MaterialButton>(R.id.todo_item_delete_button)
    private val toolbar by bind<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

    private val datePicker by lazy { createDatePicker() }
    private val priorityPopup by lazy { createPriorityPopup() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        viewModel.todoItemScreenState.repeatOnCreated(this) {
            showContent(it)
        }
    }

    private fun showContent(content: TodoItemScreenState) {

        editText.text = content.text
        saveButton.isEnabled = editText.text.isNotEmpty()

        priority.text = getString(content.priorityRes)

        content.createDate.let { createDate ->
            deleteButton.isEnabled = createDate.isNullOrBlank().not()
            deleteButton.setButtonColor(createDate.isNullOrEmpty().not())
        }

        content.deadlineDate?.let { deadline ->
            deadlineDate.text = deadline
            deadlineSwitch.isChecked = true
        }
    }


    private fun bindViews() {

        saveButton.setOnClickListener { viewModel.saveTodoItem() }

        editText.doOnTextChanged { text, _, _, _ ->
            val newText = text.toString()
            saveButton.isEnabled = newText.isNotEmpty()
            viewModel.updateTodoItemText(newText)
        }

        priorityCard.setOnClickListener { priorityPopup.show() }

        deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
            deadlineDate.visibleOrGone(isChecked)
            viewModel.onDeadlineDateActivate(isChecked)
        }

        toolbar.setNavigationOnClickListener {
            viewModel.closeTodoItem()
        }

        deadlineCard.setOnClickListener { datePicker.show(this) }

        deleteButton.setOnClickListener {
            viewModel.deleteTodoItem()
            viewModel.closeTodoItem()
        }
    }

    private fun createDatePicker(): DatePickerDialog {
        return DatePickerDialog.newInstance { viewModel.updateDeadlineDate(it) }
    }

    private fun createPriorityPopup(): PopupMenu {
        val popupMenu = PopupMenu(priority.context, priority)
        popupMenu.menuInflater.inflate(R.menu.priority_menu, popupMenu.menu)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_priority_default -> {
                    priority.text = menuItem.title
                    viewModel.updateTodoItemPriority(TodoItemPriority.DEFAULT)
                    true
                }

                R.id.menu_priority_low -> {
                    priority.text = menuItem.title
                    viewModel.updateTodoItemPriority(TodoItemPriority.LOW)
                    true
                }

                R.id.menu_priority_high -> {
                    priority.text = menuItem.title
                    viewModel.updateTodoItemPriority(TodoItemPriority.HIGH)
                    true
                }

                else -> false
            }
        }
        return popupMenu
    }
}