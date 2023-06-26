package ru.yandex.school.todoapp.presentation.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.school.todoapp.R
import ru.yandex.school.todoapp.domain.model.TodoItem
import ru.yandex.school.todoapp.domain.repository.TodoItemsRepository
import ru.yandex.school.todoapp.presentation.list.model.TodoListScreenState
import ru.yandex.school.todoapp.presentation.list.viewmodel.mapper.TodoListItemMapper
import ru.yandex.school.todoapp.presentation.navigation.AppNavigator

class TodoListViewModel(
    private val repository: TodoItemsRepository,
    private val todoListItemMapper: TodoListItemMapper,
    private val navigator: AppNavigator
) : ViewModel() {

    val todoItemsFlow = repository.todoItemsFlow

    val todoListItemsState = MutableStateFlow(TodoListScreenState.empty)
    private var todoItems: List<TodoItem> = emptyList()

    fun refreshList(items: List<TodoItem>) {
        todoItems = items
        viewModelScope.launch {
            todoListItemsState.update { previousState ->
                val completedCount: Int
                val shouldShowCompleted = previousState.isCompletedShowed

                val itemsModels = items
                    .also { completedCount = it.filter { it.isCompleted }.size }
                    .filter { item -> filterVisibleItem(shouldShowCompleted, item) }
                    .map { todoListItemMapper.map(it) }

                TodoListScreenState(
                    listItems = itemsModels,
                    completedCount = completedCount,
                    isCompletedShowed = previousState.isCompletedShowed
                )
            }
        }
    }

    private fun filterVisibleItem(shouldShowCompleted: Boolean, item: TodoItem): Boolean {
        return if (shouldShowCompleted) {
            true
        } else {
            item.isCompleted.not()
        }
    }

    fun createNewTodo() {
        navigator.openTodoItem()
    }

    fun openTodoItemInfo(todoItem: TodoItem) {
        navigator.openTodoItem(todoItem)
    }

    fun checkTodoItem(todoItem: TodoItem) {
        val checkedItem = todoItem.copy(isCompleted = todoItem.isCompleted.not())
        viewModelScope.launch {
            repository.saveTodoItem(checkedItem)
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.deleteTodoItem(todoItem)
        }
    }

    fun changeCompletedTodosVisibility() {
        todoListItemsState.update { it.copy(isCompletedShowed = it.isCompletedShowed.not()) }
        refreshList(todoItems)
    }

    fun actionOnItem(todoItem: TodoItem, resId: Int) {

        when (resId) {
            R.id.menu_action_completed -> {
                checkTodoItem(todoItem)
            }

            R.id.menu_action_edit -> {
                openTodoItemInfo(todoItem)
            }

            R.id.menu_action_delete -> {
                deleteTodoItem(todoItem)
            }
        }
    }
}