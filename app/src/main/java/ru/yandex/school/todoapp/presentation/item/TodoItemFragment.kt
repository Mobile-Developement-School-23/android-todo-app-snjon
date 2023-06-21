package ru.yandex.school.todoapp.presentation.item

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
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
    private val saveButton by bind<TextView>(R.id.todo_item_save)

    private val priorityCard by bind<LinearLayout>(R.id.todo_item_priority_item)
    private val priority by bind<TextView>(R.id.todo_item_priority_subtitle)

    private val deadlineCard by bind<ConstraintLayout>(R.id.todo_item_deadline_item)
    private val deadlineDate by bind<TextView>(R.id.todo_item_deadline_subtitle)
    private val deadlineSwitch by bind<SwitchMaterial>(R.id.todo_item_deadline_switch)

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

        priority.text = getString(content.priorityRes)

        content.createDate.let { createDate ->
            deleteButton.isEnabled = createDate.isNullOrBlank().not()
            deleteButton.setButtonColor(createDate.isNullOrEmpty().not())
        }

        content.deadlineDate?.let { deadline ->
            deadlineDate.text = deadline.toLocalizedDate()
            deadlineSwitch.isChecked = true
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
            viewModel.updateTodoItemText(newText)
        }

        priorityCard.setOnClickListener { view ->
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

        deadlineSwitch.setOnCheckedChangeListener { _, isChecked ->
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

        deadlineCard.setOnClickListener {
            showDatePickerDialog(deadlineDate)
        }

        deleteButton.setOnClickListener {
            viewModel.deleteTodoItem()
            viewModel.closeTodoItem()
        }
    }
}