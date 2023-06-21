package ru.yandex.school.todoapp.presentation.item

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.model.TodoItemPriority
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import ru.yandex.school.todoapp.presentation.item.model.TodoItemScreenState
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator
import java.util.UUID

class TodoItemViewModel(
    private val todoItemId: String?,
    private val navigator: AppNavigator,
    private val repository: TodoItemsRepository
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
                deadlineDate = todoItem.deadline,
                createDate = todoItem.createAt
            )
        }
    }

    fun updateTodoItemText(text: String) {
        todoItem = todoItem.copy(text = text)
    }

    fun updateTodoItemPriority(priority: TodoItemPriority) {
        todoItem = todoItem.copy(priority = priority)
        todoItemScreenState.update { it.copy() }
    }

    fun updateTodoItemDate(currentDate: String, deadLineDate: String?) {
        if (todoItem.createAt.isNullOrBlank()) {
            todoItem = todoItem.copy(createAt = currentDate)
        }

        todoItem = todoItem.copy(
            deadline = deadLineDate,
            modifiedAt = currentDate
        )
    }

    private fun loadTodoItem(): TodoItem {
        return if (todoItemId == null) {
            TodoItem.empty
        } else {
            repository.getTodoById(todoItemId) ?: TodoItem.empty
        }
    }

    fun saveTodoItem() {
        repository.saveTodoItem(
            todoItem.copy(
                id = todoItemId ?: UUID.randomUUID().toString()
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