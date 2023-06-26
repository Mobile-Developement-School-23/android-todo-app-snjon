package ru.yandex.school.todoapp.presentation.item.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import ru.yandex.school.todoapp.presentation.datetime.model.DateTimeModel
import ru.yandex.school.todoapp.presentation.item.model.TodoItemScreenState
import ru.yandex.school.todoapp.presentation.item.viewmodel.mapper.TodoItemDateMapper
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator
import ru.yandex.school.todoapp.presentation.util.toDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class TodoItemViewModel(
    private val todoItemId: String?,
    private val navigator: AppNavigator,
    private val repository: TodoItemsRepository,
    private val dateMapper: TodoItemDateMapper
) : ViewModel() {

    val todoItemScreenState = MutableStateFlow(TodoItemScreenState())

    private var todoItem: TodoItem = loadTodoItem()

    init {
        loadContent()
    }

    private fun loadContent() {
        todoItemScreenState.update {
            TodoItemScreenState(
                text = todoItem.text,
                priorityRes = todoItem.priority.titleRes,
                deadlineDate = todoItem.deadline?.let { dateMapper.map(it) },
                createDate = todoItem.createAt?.let { dateMapper.map(it) }
            )
        }
    }

    fun updateTodoItemText(text: String) {
        todoItem = todoItem.copy(text = text)
        todoItemScreenState.update { it.copy(text = text) }
    }

    fun updateTodoItemPriority(priority: TodoItemPriority) {
        todoItem = todoItem.copy(priority = priority)
        todoItemScreenState.update { it.copy(priorityRes = priority.titleRes) }
    }

    fun updateDeadlineDate(dateTimeModel: DateTimeModel) {
        val date = (dateTimeModel as? DateTimeModel.Date)?.toDate() ?: return

        todoItem = todoItem.copy(deadline = date)
        todoItemScreenState.update { it.copy(deadlineDate = dateMapper.map(date)) }
    }

    fun onDeadlineDateActivate(isActive: Boolean) {
        if (isActive) {
            val deadlineDate = todoItem.deadline ?: LocalDate.now()
            val deadlineDateUi = deadlineDate?.let { dateMapper.map(it) }

            todoItem = todoItem.copy(deadline = deadlineDate)
            todoItemScreenState.update { it.copy(deadlineDate = deadlineDateUi) }
        } else {
            todoItem = todoItem.copy(deadline = null)
            todoItemScreenState.update { it.copy(deadlineDate = null) }
        }
    }

    private fun loadTodoItem(): TodoItem {
        return if (todoItemId == null) {
            TodoItem.empty
        } else {
            repository.getTodoById(todoItemId) ?: TodoItem.empty
        }
    }

    fun saveTodoItem() {
        val isCreating = todoItemId == null
        val currentDate = LocalDate.now()
        val currentDateTime = LocalDateTime.now()

        repository.saveTodoItem(
            todoItem.copy(
                id = todoItemId ?: UUID.randomUUID().toString(),
                createAt = if (isCreating) currentDate else todoItem.createAt,
                modifiedAt = currentDateTime
            )
        )
        navigator.openTodoList()
    }


    fun closeTodoItem() {
        navigator.openTodoList()
    }

    fun deleteTodoItem() {
        repository.deleteTodoItem(todoItem)
    }
}