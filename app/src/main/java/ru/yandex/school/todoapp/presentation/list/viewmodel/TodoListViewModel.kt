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

    val todoListItemsState = MutableStateFlow(TodoListScreenState.empty)

    init {
        refreshList()
    }

    fun refreshList() {
        viewModelScope.launch {
            todoListItemsState.update { previousState ->
                val completedCount: Int
                val shouldShowCompleted = previousState.isCompletedShowed

                val items = repository.getTodoItems()
                    .value
                    .map { it.value }
                    .also { completedCount = it.filter { it.isCompleted }.size }
                    .filter { item -> filterVisibleItem(shouldShowCompleted, item) }
                    .map { todoListItemMapper.map(it) }

                TodoListScreenState(
                    listItems = items,
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
            refreshList()
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.deleteTodoItem(todoItem)
            refreshList()
        }
    }

    fun changeCompletedTodosVisibility() {
        todoListItemsState.update {
            TodoListScreenState(
                listItems = it.listItems,
                completedCount = it.completedCount,
                isCompletedShowed = it.isCompletedShowed.not()
            )
        }
        refreshList()
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