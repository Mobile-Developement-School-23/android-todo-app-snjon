package ru.yandex.school.todoapp.presentation.item

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import ru.yandex.school.todoapp.presentation.item.model.TodoItemScreenState
import ru.yandex.school.todoapp.presentation.navigation.KEY_TODO_ITEM_ID
import ru.yandex.school.todoapp.presentation.util.bind
import ru.yandex.school.todoapp.presentation.util.convertFromLocalizedDateFormat
import ru.yandex.school.todoapp.presentation.util.getCurrentDate
import ru.yandex.school.todoapp.presentation.util.repeatOnCreated
import ru.yandex.school.todoapp.presentation.util.setButtonColor
import ru.yandex.school.todoapp.presentation.util.showDatePickerDialog
import ru.yandex.school.todoapp.presentation.util.toLocalizedDate
import ru.yandex.school.todoapp.presentation.util.visibleOrInvisible
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodoItemFragment : Fragment(R.layout.fragment_todo_item) {

    private val viewModel: TodoItemViewModel by viewModel {
        parametersOf(
            requireArguments().getString(KEY_TODO_ITEM_ID)
        )
    }

    private val editText by bind<TextView>(R.id.todo_item_text)
    private val priority by bind<TextView>(R.id.todo_item_priority)
    private val saveButton by bind<TextView>(R.id.todo_item_save)
    private val deadlineDate by bind<TextView>(R.id.todo_item_date)
    private val todoBeforeSwitch by bind<SwitchMaterial>(R.id.todo_item_switch)
    private val deleteButton by bind<MaterialButton>(R.id.todo_item_delete_button)
    private val toolbar by bind<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        subscribeOnViewModel()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun subscribeOnViewModel() {
        viewModel.todoItemScreenState.repeatOnCreated(this) {
            showContent(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun showContent(content: TodoItemScreenState) {

        editText.text = content.text
        saveButton.isEnabled = editText.text.isNotEmpty()
        deleteButton.isEnabled = editText.text.isNotEmpty()
        deleteButton.setButtonColor(editText.text.isNotEmpty())

        priority.text = getString(content.priorityRes)

        content.deadlineDate?.let { date ->
            deadlineDate.text = date.toLocalizedDate()
            todoBeforeSwitch.isChecked = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveDate() {
        val currentDate = getCurrentDate()
        val deadlineDate = if (deadlineDate.text.toString().isNotEmpty()) {
            deadlineDate.text.toString().convertFromLocalizedDateFormat()
        } else {
            null
        }

        viewModel.updateTodoItemDate(currentDate, deadlineDate)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun bindViews() {

        saveButton.setOnClickListener {
            saveDate()
            viewModel.saveTodoItem()
        }

        editText.doOnTextChanged { text, _, _, _ ->
            val newText = text.toString()
            saveButton.isEnabled = newText.isNotEmpty()
            deleteButton.isEnabled = newText.isNotEmpty()
            deleteButton.setButtonColor(newText.isNotEmpty())
            viewModel.updateTodoItemText(newText)
        }

        priority.setOnClickListener { view ->
            val popupMenu = PopupMenu(view.context, view)
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
            popupMenu.show()
        }

        todoBeforeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                deadlineDate.visibleOrInvisible(true)
                if (deadlineDate.text == "") {
                    val currentDate = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    deadlineDate.text = currentDate.format(formatter).toLocalizedDate()
                }
            } else {
                deadlineDate.visibleOrInvisible(false)
                deadlineDate.text = ""
            }
        }

        toolbar.setNavigationOnClickListener {
            viewModel.closeTodoItem()
        }

        deadlineDate.setOnClickListener {
            showDatePickerDialog(deadlineDate)
        }

        deleteButton.setOnClickListener {
            viewModel.deleteTodoItem()
            viewModel.closeTodoItem()
        }
    }
}